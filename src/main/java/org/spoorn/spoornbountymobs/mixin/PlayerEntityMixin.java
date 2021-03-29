package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spoorn.spoornbountymobs.entity.EntityDataComponent;
import org.spoorn.spoornbountymobs.entity.PlayerDataComponent;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    /**
     * For testing player data persistence.
     */
    /*@Inject(method = "attack", at = @At(value = "HEAD"))
    public void testPlayerData(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        System.out.println("attacking player: " + SpoornBountyEntityRegistry.PLAYER_DATA.get(player));
    }*/

    @Inject(method = "onKilledOther", at = @At(value = "TAIL"))
    public void incrementBountyCount(ServerWorld serverWorld, LivingEntity livingEntity, CallbackInfo ci) {
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(livingEntity)) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(livingEntity);
            PlayerDataComponent playerDataComponent = SpoornBountyMobsUtil.getPlayerDataComponent(player);
            playerDataComponent.incrementBountyKillCount(entityDataComponent.getSpoornBountyTier());
        }
    }
}
