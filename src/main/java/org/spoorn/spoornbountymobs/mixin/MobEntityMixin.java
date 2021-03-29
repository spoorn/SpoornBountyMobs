package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.entity.SpoornEntityDataComponent;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {

    @Shadow protected int experiencePoints;

    /**
     * Scale experience according to Bounty mob's tier.
     *
     * For some reason injecting into the TAIL of the method doesn't work.  I think it's because the method has
     * multiple return points.
     */
    @Inject(method = "getCurrentExperience", at = @At(value = "HEAD"), cancellable = true)
    public void scaleExperience(PlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        MobEntity mobEntity = (MobEntity) (Object) this;
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(mobEntity)) {
            SpoornEntityDataComponent component = SpoornBountyMobsUtil.getSpoornEntityDataComponent(mobEntity);

            //System.out.println("increase exp from " + this.experiencePoints + " to " + this.experiencePoints * component.getSpoornBountyTier().getExperienceScale());
            this.experiencePoints = this.experiencePoints * component.getSpoornBountyTier().getExperienceScale();
        }
    }
}
