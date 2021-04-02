package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.entity.EntityDataComponent;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow private EntityDimensions dimensions;

    @Shadow public abstract Box getBoundingBox();

    @Shadow public abstract void setBoundingBox(Box boundingBox);

    /**
     * Resize entity bounding box if it has a bounty.
     */
    @Inject(method = "getDimensions", at = @At(value = "TAIL"), cancellable = true)
    private void resizeEntity(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        Entity entity = (Entity) (Object) this;
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(entity)) {
            EntityDimensions dimensions = cir.getReturnValue();
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(entity);
            float scale = entityDataComponent.getSpoornBountyTier().getMobSizeScale();
            EntityDimensions newDimensions = dimensions.scaled(scale);
            this.dimensions = newDimensions;
            Box box = this.getBoundingBox();
            this.setBoundingBox(new Box(box.minX, box.minY, box.minZ, box.minX + (double)newDimensions.width,
                box.minY + (double)newDimensions.height, box.minZ + (double)newDimensions.width));
            cir.setReturnValue(dimensions.scaled(scale));
        }
    }

    /**
     * Apply mob status attacks.
     */
    @Inject(method = "dealDamage", at=@At(value = "TAIL"))
    public void applyStatusEffects(LivingEntity attacker, Entity target, CallbackInfo ci) {
        if ((target instanceof LivingEntity) && SpoornBountyMobsUtil.entityIsHostileAndHasBounty(attacker)) {
            EntityDataComponent component = SpoornBountyMobsUtil.getSpoornEntityDataComponent(attacker);
            LivingEntity livingEntity = (LivingEntity) target;
            if (component.hasWeaknessAttack()) {
                livingEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstance(StatusEffects.WEAKNESS, 100, 1));
            }
            if (component.hasWitherAttack()) {
                livingEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstance(StatusEffects.WITHER, 100, 1));
            }
            if (component.hasBlindnessAttack()) {
                livingEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstance(StatusEffects.BLINDNESS, 100, 1));
            }
            if (component.hasPoisonAttack()) {
                livingEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstance(StatusEffects.POISON, 100, 1));
            }
            if (component.hasSlownessAttack()) {
                livingEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstance(StatusEffects.SLOWNESS, 100, 1));
            }
            if (component.hasHungerAttack()) {
                livingEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstance(StatusEffects.HUNGER, 100, 1));
            }
            if (component.hasBurningAttack()) {
                livingEntity.setOnFireFor(5);
            }
        }
    }
}
