package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "getMaxHealth", at = @At(value = "TAIL"), cancellable = true)
    public void setMaxHealthForBountyMob(CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(livingEntity)) {
            cir.setReturnValue(cir.getReturnValue() +
                SpoornBountyMobsUtil.getSpoornEntityDataComponent(livingEntity).getSpoornBountyTier().getMaxHealthIncrease()
                + SpoornBountyMobsUtil.getSpoornEntityDataComponent(livingEntity).getBonusHealth());
        }
    }
}
