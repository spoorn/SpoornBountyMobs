package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.config.ModConfig;
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
        if (SpoornBountyMobsUtil.isHostileEntity(entity) && SpoornBountyMobsUtil.entityHasBounty(entity)) {
            EntityDimensions dimensions = cir.getReturnValue();
            EntityDimensions newDimensions = dimensions.scaled((float) ModConfig.get().bountyMobSizeScale);
            this.dimensions = newDimensions;
            Box box = this.getBoundingBox();
            this.setBoundingBox(new Box(box.minX, box.minY, box.minZ, box.minX + (double)newDimensions.width,
                box.minY + (double)newDimensions.height, box.minZ + (double)newDimensions.width));
            cir.setReturnValue(dimensions.scaled((float) ModConfig.get().bountyMobSizeScale));
        }
    }
}
