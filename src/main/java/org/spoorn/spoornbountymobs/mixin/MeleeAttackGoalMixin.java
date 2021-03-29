package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

/**
 * Manipulate Attack Goal for Melee entities.
 */
@Mixin(MeleeAttackGoal.class)
public class MeleeAttackGoalMixin {

    @Shadow @Final protected PathAwareEntity mob;

    /**
     * Update entity's attack range based on dimensions if it has a bounty.
     */
    @Inject(method = "getSquaredMaxAttackDistance", at = @At(value = "TAIL"), cancellable = true)
    private void changeAttackDistanceForBountyMobs(LivingEntity entity, CallbackInfoReturnable<Double> cir) {
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(entity)) {
            cir.setReturnValue((double) (Math.sqrt(Math.pow(this.mob.getWidth()*2,2) + Math.pow(this.mob.getHeight()*2,2))*2
                    + entity.getWidth()));
        }
    }
}
