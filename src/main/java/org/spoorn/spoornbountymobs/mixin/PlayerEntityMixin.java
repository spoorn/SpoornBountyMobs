package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.entity.component.EntityDataComponent;
import org.spoorn.spoornbountymobs.entity.component.PlayerDataComponent;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;
import org.spoorn.spoornbountymobs.util.DropDistributionData;
import org.spoorn.spoornbountymobs.util.ItemInfo;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private static final MutableText BROADCAST = Text.translatable("sbm.broadcast.levelup").formatted(Formatting.WHITE);

    private static final MutableText TAKEDOWN_BROADCAST_1 = Text.translatable("sbm.broadcast.playerkillbounty.part1").formatted(Formatting.WHITE);
    private static final MutableText TAKEDOWN_BROADCAST_2 = Text.translatable("sbm.broadcast.playerkillbounty.part2").formatted(Formatting.WHITE);

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
    public void incrementBountyCount(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(other)) {
            PlayerEntity player = (PlayerEntity) (Object) this;
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(other);
            PlayerDataComponent playerDataComponent = SpoornBountyMobsUtil.getPlayerDataComponent(player);
            playerDataComponent.incrementBountyKillCount(entityDataComponent.getSpoornBountyTier());

            // Update player's highest tier if increased
            int highestLevel = playerDataComponent.getHighestBountyHunterLevel();
            int currLevel = SpoornBountyMobsUtil.getBountyHunterLevel(player);
            if (currLevel > highestLevel) {
                playerDataComponent.setHighestBountyHunterLevel(currLevel);
                try {
                    if (ModConfig.get().broadcastMessageWhenBountyLevelUp) {
                        String playerName = player.getDisplayName().getString();
                        List<String> broadcastDisabled = ModConfig.get().broadcastDisabled;
                        if (!broadcastDisabled.contains(playerName)) {
                            MutableText playerpart = Text.literal(playerName).formatted(Formatting.DARK_AQUA);
                            MutableText levelpart = Text.literal(Integer.toString(currLevel)).formatted(Formatting.LIGHT_PURPLE);
                            player.getServer().getPlayerManager().broadcast(playerpart.append(BROADCAST).append(levelpart), false);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[SpoornBountyMobs] Error broadcasting SpoornBountyMobs level up: " + e);
                }
            }

            //System.out.println(Registry.ENTITY_TYPE.getId(livingEntity.getType()));
            // drop loot from the bounty mob if applicable
            SpoornBountyTier tier = entityDataComponent.getSpoornBountyTier();
            String entityId = Registry.ENTITY_TYPE.getId(other.getType()).toString();
            List<DropDistributionData> dropDists = SpoornBountyEntityRegistry.DROP_REGISTRY.get(tier);
            DropDistributionData dropDist = SpoornBountyMobsUtil.findPatternInMap(tier, entityId, dropDists);
            if (dropDist != null) {
                //System.out.println("rolling " + dropDist.rolls + " times");
                for (int i = 0; i < dropDist.rolls; i++) {
                    if (SpoornBountyMobsUtil.RANDOM.nextDouble() < dropDist.dropChance) {
                        String sampledItemRegex = dropDist.itemDrops.sample();

                        ItemInfo itemInfo = SpoornBountyEntityRegistry.CACHED_ITEM_REGISTRY.get(sampledItemRegex);
                        List<Item> matchingItems = itemInfo.items;
                        //System.out.println("matching items: " + matchingItems);
                        if (matchingItems == null || matchingItems.isEmpty()) {
                            System.err.println("[SpoornBountyMobs] Configuration specified item \"" + sampledItemRegex + "\" " +
                                    "did not match any item in the registry!  Did you configure SpoornBountyMobs drops correctly?");
                        } else {
                            Item itemToDrop = SpoornBountyMobsUtil.sampleFromList(matchingItems);

                            // Get count, and NBT for the item.  We already have the matching list of items from above
                            ItemStack itemStackToDrop = new ItemStack(itemToDrop, itemInfo.count);
                            if (itemInfo.nbt != null) {
                                itemStackToDrop.setNbt(itemInfo.nbt);
                            }
                            other.dropStack(itemStackToDrop);
                            //System.out.println("dropped item " + itemToDrop + " from " + livingEntity);
                        }
                    }
                }
            }

            // Sync new player data to clients
            SpoornBountyEntityRegistry.PLAYER_DATA.sync(player);

            if (ModConfig.get().broadcastMessageWhenPlayerKillBountyMob) {
                try {
                    String playerName = player.getDisplayName().getString();
                    List<String> broadcastDisabled = ModConfig.get().broadcastDisabled;
                    if (!broadcastDisabled.contains(playerName) && !broadcastDisabled.contains(Registry.ENTITY_TYPE.getId(other.getType()).toString())) {
                        MutableText playerpart = Text.literal(playerName).formatted(Formatting.DARK_AQUA);
                        MutableText tierpart = Text.literal(tier.getTierType().getName()).formatted(tier.getTierType().getFormattings());
                        MutableText mobpart = Text.translatable(other.getDisplayName().getString()).formatted(Formatting.DARK_GREEN);
                        player.getServer().getPlayerManager()
                                .broadcast(playerpart.append(TAKEDOWN_BROADCAST_1).append(tierpart).append(TAKEDOWN_BROADCAST_2).append(mobpart),
                                        false);
                    }
                } catch (Exception e) {
                    System.err.println("Exception while trying to broadcast player killed bounty mob message for SpoornBountyMobs: " + e);
                }
            }
        }
    }
}
