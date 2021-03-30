package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.PlayerDataComponent;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    /**
     * Update player bounty score upon death and sync to clients.
     *
     * TODO: Optimize player data over network by only updating bounty score.
     */
    @Inject(method = "onDeath", at = @At(value = "HEAD"))
    public void bountyScorePenaltyOnDeath(DamageSource source, CallbackInfo ci) {
        if (ModConfig.get().playerDeathBountyScorePenalty > 0) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            PlayerDataComponent playerDataComponent = SpoornBountyMobsUtil.getPlayerDataComponent(player);
            double bountyScore = playerDataComponent.getBountyScore();
            bountyScore = bountyScore - (bountyScore * (ModConfig.get().playerDeathBountyScorePenalty/100.0));
            playerDataComponent.setBountyScore(bountyScore);
            //System.out.println("new bounty score: " + bountyScore);
            SpoornBountyEntityRegistry.PLAYER_DATA.sync(player);
        }
    }
}
