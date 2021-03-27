package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "getDimensions", at = @At(value = "TAIL"), cancellable = true)
    private void resizeEntity(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        Entity entity = (Entity) (Object) this;
        if (SpoornBountyMobsUtil.isHostileEntity(entity)) {
            EntityDimensions dimensions = cir.getReturnValue();
            cir.setReturnValue(dimensions.scaled((float)ModConfig.get().bountyMobSizeScale));
        }
    }
}
