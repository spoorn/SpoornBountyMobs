package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

/**
 * Manipulate Spider Entity's Attack Goal.
 */
@Mixin(targets = {"net.minecraft.entity.mob.SpiderEntity$AttackGoal"})
public class SpiderEntityAttackGoalMixin {

    /**
     * Update entity's attack range based on dimensions if it has a bounty.
     */
    @Inject(method = "getSquaredMaxAttackDistance", at = @At(value = "TAIL"), cancellable = true)
    private void changeAttackDistanceForBountyMobs(LivingEntity entity, CallbackInfoReturnable<Double> cir) {
        MeleeAttackGoalAccessorMixin meleeAttackGoal = (MeleeAttackGoalAccessorMixin) (Object) this;
        Entity mob = meleeAttackGoal.getMob();
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(mob)) {
            //System.out.println("changed spider range");
            cir.setReturnValue((double) (Math.sqrt(Math.pow(mob.getWidth()*2,2) + Math.pow(mob.getHeight()*2,2))*2
                + entity.getWidth()));
        }
    }
}
