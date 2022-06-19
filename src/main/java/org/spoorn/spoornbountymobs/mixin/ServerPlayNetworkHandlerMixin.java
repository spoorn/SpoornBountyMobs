package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spoorn.spoornbountymobs.entity.component.EntityDataComponent;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @ModifyConstant(method = "onPlayerInteractEntity", constant = @Constant(doubleValue = 36.0D))
    private double scaleMaxDistanceForInteractingWithEntity(double constant, PlayerInteractEntityC2SPacket packet) {
        ServerWorld serverWorld = this.player.getWorld();
        final Entity entity = packet.getEntity(serverWorld);
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(entity)) {
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(entity);
            float scale = entityDataComponent.getSpoornBountyTier().getMobSizeScale();
            if (scale > 1) {
                return constant * scale * scale;
            }
        }
        
        return constant;
    }
}
