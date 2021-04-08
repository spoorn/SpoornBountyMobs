package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(Entity.class)
public class EntityMixinClient {


    /**
     * Change outline color for Bounty mobs.
     */
    @Inject(method = "getTeamColorValue", at = @At(value = "TAIL"), cancellable = true)
    public void changeBountyMobOutlineColor(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(entity)) {
            SpoornBountyTier tier = SpoornBountyMobsUtil.getSpoornEntityDataComponent(entity).getSpoornBountyTier();
            cir.setReturnValue(tier.getGlowColor());
        }
    }
}
