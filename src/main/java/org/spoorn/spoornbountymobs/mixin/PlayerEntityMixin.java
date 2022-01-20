package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.MessageType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.EntityDataComponent;
import org.spoorn.spoornbountymobs.entity.PlayerDataComponent;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

import java.util.Map;
import java.util.regex.Pattern;

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
            int highestLevel = playerDataComponent.getHighestBountyHunterTier();
            int currLevel = SpoornBountyMobsUtil.getBountyHunterTier(player);
            if (currLevel > highestLevel) {
                playerDataComponent.setHighestBountyHunterTier(currLevel);
                try {
                    if (ModConfig.get().broadcastMessageWhenBountyLevelUp) {
                        MutableText playerpart = new LiteralText(player.getDisplayName().getString()).formatted(Formatting.DARK_AQUA);
                        MutableText levelpart = new LiteralText(Integer.toString(currLevel)).formatted(Formatting.LIGHT_PURPLE);
                        player.getServer().getPlayerManager().broadcastChatMessage(playerpart.append(BROADCAST).append(levelpart), MessageType.CHAT, Util.NIL_UUID);
                    }
                } catch (Exception e) {
                    System.err.println("Error broadcasting SpoornBountyMobs level up: " + e);
                }
            }

            //System.out.println(Registry.ENTITY_TYPE.getId(livingEntity.getType()));
            // drop loot from the bounty mob if applicable
            String entityId = Registry.ENTITY_TYPE.getId(livingEntity.getType()).toString();
            Map<Pattern, Pair<Double, EnumeratedDistribution<String>>> dropDistributions =
                    SpoornBountyEntityRegistry.DROP_REGISTRY.get(entityDataComponent.getSpoornBountyTier());
            Pair<Double, EnumeratedDistribution<String>> dropDist = SpoornBountyMobsUtil.findPatternInMap(entityId, dropDistributions);
            if (dropDist != null && SpoornBountyMobsUtil.RANDOM.nextDouble() < dropDist.getKey()) {
                Item itemToDrop = Registry.ITEM.get(new Identifier(dropDist.getValue().sample()));
                livingEntity.dropItem(itemToDrop);
                //System.out.println("dropped item " + itemToDrop + " from " + livingEntity);
            }

            // Sync new player data to clients
            SpoornBountyEntityRegistry.PLAYER_DATA.sync(player);
        }
    }
}
