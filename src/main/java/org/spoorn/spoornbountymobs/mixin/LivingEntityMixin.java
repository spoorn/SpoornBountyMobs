package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.EntityDataComponent;
import org.spoorn.spoornbountymobs.entity.PlayerDataComponent;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    @Shadow @Final private static TrackedData<Float> HEALTH;

    /**
     * Change entity's max health based on Bounty attributes such as Bounty Hunter Tier for player, or Bounty Tier for
     * mob.
     */
    @Inject(method = "getMaxHealth", at = @At(value = "TAIL"), cancellable = true)
    public void setMaxHealthForBountyMob(CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;

        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(livingEntity)) {
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(livingEntity);
            // Base bonus health + bonus health from player bounty hunter tiers
            cir.setReturnValue(cir.getReturnValue()
                + entityDataComponent.getSpoornBountyTier().getMaxBaseHealthIncrease()
                + entityDataComponent.getBonusBountyTierHealth());
        } else if (SpoornBountyMobsUtil.isPlayerEntity(livingEntity)) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            // Add bonus health to player based on their highest bounty hunter tier
            int highestTier = SpoornBountyMobsUtil.getPlayerDataComponent(player).getHighestBountyHunterTier();
            //System.out.println("old val: " + cir.getReturnValue());
            //System.out.println("maxhealth: " + (cir.getReturnValue() +
            //        ((float)ModConfig.get().playerBonusHealthPerBountyHunterTier * currTier)));
            cir.setReturnValue(cir.getReturnValue() +
                ((float)ModConfig.get().playerBonusHealthPerBountyHunterTier * highestTier));
        }
    }

    /**
     * Increase player attack based on how player's Bounty Hunter tier.
     */
    @Inject(method = "getAttributeValue", at = @At(value = "TAIL"), cancellable = true)
    public void overridePlayerAttack(EntityAttribute attribute, CallbackInfoReturnable<Double> cir) {
        if (attribute.equals(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            if (livingEntity instanceof PlayerEntity) {
                double bonusDamage = SpoornBountyMobsUtil.getPlayerBonusDamage((PlayerEntity) livingEntity);
                cir.setReturnValue(cir.getReturnValue() + bonusDamage);
            }
        }
    }
}
