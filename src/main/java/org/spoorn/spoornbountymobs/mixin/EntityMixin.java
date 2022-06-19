package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Box;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.entity.component.EntityDataComponent;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(Entity.class)
public abstract class EntityMixin {
    
    private Logger log = LogManager.getLogger("SpoornBountyMobsEntityMixin");

    @Shadow private EntityDimensions dimensions;

    @Shadow public abstract void setBoundingBox(Box boundingBox);

    @Shadow protected abstract boolean doesNotCollide(Box box);

    @Shadow private Box boundingBox;

    /**
     * Resize entity bounding box if it has a bounty.
     */
    @Inject(method = "getDimensions", at = @At(value = "TAIL"), cancellable = true)
    private void resizeEntity(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        Entity entity = (Entity) (Object) this;
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(entity)) {
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(entity);
            float scale = entityDataComponent.getSpoornBountyTier().getMobSizeScale();
            if (scale > 1) {
                EntityDimensions dimensions = cir.getReturnValue();
                EntityDimensions newDimensions = dimensions.scaled(scale, scale);
                Box box = this.boundingBox;
                double distX = box.maxX - box.minX;
                double distZ = box.maxZ - box.minZ;
                double diffX = distX / 2 * scale - distX / 2;
                double diffZ = distZ / 2 * scale - distZ / 2;
                Box newBox = new Box(box.minX - diffX, box.minY, box.minZ - diffZ, box.maxX + diffX, box.minY + newDimensions.height, box.maxZ + diffZ);
                if (this.doesNotCollide(newBox)) {
                    this.setBoundingBox(newBox);
                    this.dimensions = newDimensions;
                    cir.setReturnValue(newDimensions);
                    cir.cancel();
                } else {
                    entityDataComponent.setSpoornBountyTier(entityDataComponent.getSpoornBountyTier().toBuilder().mobSizeScale(1.0f).build());
                    SpoornBountyEntityRegistry.HOSTILE_ENTITY_DATA.sync(entity);
                    log.warn("Entity {} could not increase scale as it collides with terrain/entities.", entity);
                }
            }
        }
    }

    /**
     * Apply mob status attacks.
     */
    @Inject(method = "applyDamageEffects", at=@At(value = "TAIL"))
    public void applyStatusEffects(LivingEntity attacker, Entity target, CallbackInfo ci) {
        if ((target instanceof LivingEntity) && SpoornBountyMobsUtil.entityIsHostileAndHasBounty(attacker)) {
            EntityDataComponent component = SpoornBountyMobsUtil.getSpoornEntityDataComponent(attacker);
            LivingEntity livingEntity = (LivingEntity) target;
            // TODO: Make durations configurable
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
