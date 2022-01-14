package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spoorn.spoornbountymobs.entity.component.EntityDataComponent;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    /**
     * Scale entity size rendering if it has a bounty.
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V"))
    public void scaleEntityRendering(MatrixStack matrixStack, float x, float y, float z, T livingEntity,
        float f, float g, MatrixStack matrixStack2, VertexConsumerProvider vertexConsumerProvider, int i) {

        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(livingEntity)) {
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(livingEntity);
            float scale = entityDataComponent.getSpoornBountyTier().getMobSizeScale();
            matrixStack.scale(-scale, -scale, scale);
        } else {
            matrixStack.scale(x, y, z);
        }
    }
}
