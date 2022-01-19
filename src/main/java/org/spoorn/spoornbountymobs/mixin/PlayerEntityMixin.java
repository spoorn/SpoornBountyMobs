package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.entity.component.EntityDataComponent;
import org.spoorn.spoornbountymobs.entity.component.PlayerDataComponent;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private static final MutableText BROADCAST = new TranslatableText("sbm.broadcast.levelup").formatted(Formatting.WHITE);

    /**
     * For testing player data persistence.
     */
    /*@Inject(method = "attack", at = @At(value = "HEAD"))
    public void testPlayerData(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        System.out.println("player: " + SpoornBountyEntityRegistry.PLAYER_DATA.get(player));
    }*/

    /**
     * Increment player's bounty kill count and score upon killing a Bounty mob.
     */
    @Inject(method = "onKilledOther", at = @At(value = "TAIL"))
    public void incrementBountyCount(ServerWorld serverWorld, LivingEntity livingEntity, CallbackInfo ci) {
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(livingEntity)) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(livingEntity);
            PlayerDataComponent playerDataComponent = SpoornBountyMobsUtil.getPlayerDataComponent(player);
            playerDataComponent.incrementBountyKillCount(entityDataComponent.getSpoornBountyTier());

            // Update player's highest tier if increased
            int highestLevel = playerDataComponent.getHighestBountyHunterLevel();
            int currLevel = SpoornBountyMobsUtil.getBountyHunterLevel(player);
            if (currLevel > highestLevel) {
                playerDataComponent.setHighestBountyHunterLevel(currLevel);
                try {
                    if (ModConfig.get().broadcastMessageWhenBountyLevelUp) {
                        MutableText playerpart = new LiteralText(player.getDisplayName().getString()).formatted(Formatting.DARK_AQUA);
                        MutableText levelpart = new LiteralText(Integer.toString(currLevel)).formatted(Formatting.LIGHT_PURPLE);
                        player.getServer().getPlayerManager().broadcast(playerpart.append(BROADCAST).append(levelpart), MessageType.CHAT, Util.NIL_UUID);
                    }
                } catch (Exception e) {
                    System.err.println("Error broadcasting SpoornBountyMobs level up: " + e);
                }
            }

            // Sync new player data to clients
            SpoornBountyEntityRegistry.PLAYER_DATA.sync(player);
        }
    }
}
