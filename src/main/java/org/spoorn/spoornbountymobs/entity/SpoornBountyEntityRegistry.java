package org.spoorn.spoornbountymobs.entity;

import static org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil.ITEM_REGEX;
import static org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil.getStatusEffectInstance;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.spoorn.spoornbountymobs.config.Drop;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.component.EntityDataComponent;
import org.spoorn.spoornbountymobs.entity.component.PlayerDataComponent;
import org.spoorn.spoornbountymobs.entity.component.SpoornBountyHostileEntityDataComponent;
import org.spoorn.spoornbountymobs.entity.component.SpoornBountyPlayerDataComponent;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;
import org.spoorn.spoornbountymobs.util.DropDistributionData;
import org.spoorn.spoornbountymobs.util.ItemInfo;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Bounty Entity registry.  All things related to registering Bounties on Entities when player starts tracking one.
 */
@Log4j2
public class SpoornBountyEntityRegistry implements EntityComponentInitializer {

    private static final double ZERO = 0.0;
    private static final double ONE = 1.0;

    private static final MutableText BROADCAST_1 = Text.translatable("sbm.broadcast.part1").formatted(Formatting.WHITE);
    private static final MutableText BROADCAST_2 = Text.translatable("sbm.broadcast.part2").formatted(Formatting.WHITE);

    // Hostile Entity data
    public static final ComponentKey<EntityDataComponent> HOSTILE_ENTITY_DATA =
            ComponentRegistryV3.INSTANCE.getOrCreate(EntityDataComponent.ID, EntityDataComponent.class);
    // Player data
    public static final ComponentKey<PlayerDataComponent> PLAYER_DATA =
            ComponentRegistryV3.INSTANCE.getOrCreate(PlayerDataComponent.ID, PlayerDataComponent.class);

    // This maps to a List of DropDistributionData so that when there are conflicting configurations, we take the first one
    // specified in the config file.
    public static final Map<SpoornBountyTier, List<DropDistributionData>> DROP_REGISTRY = new HashMap<>();
    public static final Map<String, ItemInfo> CACHED_ITEM_REGISTRY = new HashMap<>();

    private static final Set<String> CONFIGURED_DROPS = ConcurrentHashMap.newKeySet();

    public static void init() {
        registerStartTrackingCallback();
        DROP_REGISTRY.clear();  // Just in case mod is reloaded dynamically
        generateDropRegistry();
        registerServerStartedCallback();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Entity data
        registry.registerFor(HostileEntity.class, HOSTILE_ENTITY_DATA, SpoornBountyHostileEntityDataComponent::new);

        // Player data, should be persisted across sessions
        registry.registerForPlayers(PLAYER_DATA, SpoornBountyPlayerDataComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }

    // Generates a cached registry of all configured drops.  Requires ModConfig and SpoornBountyTiers to have already been initialized.
    private static void generateDropRegistry() {
        List<Drop> commonDrops = ModConfig.get().COMMON_TIER.drops;
        List<Drop> uncommonDrops = ModConfig.get().UNCOMMON_TIER.drops;
        List<Drop> rareDrops = ModConfig.get().RARE_TIER.drops;
        List<Drop> epicDrops = ModConfig.get().EPIC_TIER.drops;
        List<Drop> legendaryDrops = ModConfig.get().LEGENDARY_TIER.drops;
        List<Drop> doomDrops = ModConfig.get().DOOM_TIER.drops;

        addToDropRegistry(SpoornBountyTier.COMMON, commonDrops);
        addToDropRegistry(SpoornBountyTier.UNCOMMON, uncommonDrops);
        addToDropRegistry(SpoornBountyTier.RARE, rareDrops);
        addToDropRegistry(SpoornBountyTier.EPIC, epicDrops);
        addToDropRegistry(SpoornBountyTier.LEGENDARY, legendaryDrops);
        addToDropRegistry(SpoornBountyTier.DOOM, doomDrops);

        //System.out.println("Drop Registry: " + DROP_REGISTRY);
    }

    private static void addToDropRegistry(SpoornBountyTier tier, List<Drop> drops) {
        List<DropDistributionData> dropDists = new ArrayList<>();
        for (Drop drop : drops) {
            EnumeratedDistribution<String> dropDistribution = new EnumeratedDistribution(drop.items.stream().map(e -> {
                CONFIGURED_DROPS.add(e.item);
                return Pair.create(e.item, (double) e.weight);
            }).collect(Collectors.toList()));

            Pattern regex = Pattern.compile(drop.entityId);
            dropDists.add(new DropDistributionData(regex, SpoornBountyMobsUtil.bound(drop.dropChance, ZERO, ONE), Math.max(drop.rolls, 0), dropDistribution));
        }
        DROP_REGISTRY.put(tier, dropDists);
    }

    private static void registerServerStartedCallback() {
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            // Cache all registered items that match all configured drop regexes
            for (String item : CONFIGURED_DROPS) {
                Matcher matcher = ITEM_REGEX.matcher(item.trim());
                
                if (!matcher.matches()) {
                    throw new RuntimeException("[SpoornBountyMobs] [SpoornBountyEntityRegistry] Item regex {" + item.trim() + "} is not in a valid format.  " +
                            "Please check the config file at config/spoornbountymobs.json5 for acceptable formats.");
                }

                // Middle of regex is the item regex
                Pattern regex = Pattern.compile(matcher.group("item"));
                List<Item> matchingItems = Registries.ITEM.stream()
                        .filter(e -> regex.asMatchPredicate().test(Registries.ITEM.getId(e).toString()))
                        .collect(Collectors.toList());
                
                // Count and NBT
                String countStr = matcher.group("count");
                int count = countStr == null ? 1 : Integer.parseInt(countStr.trim());
                String nbtStr = matcher.group("nbt");

                NbtCompound nbtCompound = null;
                if (nbtStr != null) {
                    try {
                        nbtCompound = StringNbtReader.parse(nbtStr);
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException("[SpoornBountyMobs] Could not read Nbt compound from \"" + item + "\"");
                    }
                }
                
                CACHED_ITEM_REGISTRY.putIfAbsent(item, new ItemInfo(count, matchingItems, nbtCompound));
            }

            // Free up CONFIGURED_DROPS as it's not needed anymore
            CONFIGURED_DROPS.clear();
            //System.out.println("CachedItemRegistry:" + CACHED_ITEM_REGISTRY);
        });
    }

    // On entity being tracked by a player, chance to mark the entity as having a bounty.  Update entity data
    private static void registerStartTrackingCallback() {
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            double spawnChance = ModConfig.get().bountyChance;
            if (spawnChance > 0 && SpoornBountyMobsUtil.isHostileEntity(trackedEntity)) {
                HostileEntity hostileEntity = (HostileEntity) trackedEntity;
                EntityDataComponent component = SpoornBountyMobsUtil.getSpoornEntityDataComponent(hostileEntity);

                if (!component.hasTracked() && (SpoornBountyMobsUtil.RANDOM.nextFloat() < (1.0/ ModConfig.get().bountyChance))) {
                    // Set Entity data
                    component.setHasBounty(true);
                    component.setBonusBountyLevelHealth(SpoornBountyMobsUtil.getHealthIncreaseFromBountyScore(player, hostileEntity));

                    SpoornBountyTier tier = SpoornBountyMobsUtil.SPOORN_BOUNTY_TIERS.sample();
                    component.setSpoornBountyTier(tier);

                    // Set special attacks
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceWeaknessAttack()) {
                        component.setHasWeaknessAttack(true);
                    }
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceWitherAttack()) {
                        component.setHasWitherAttack(true);
                    }
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceBlindnessAttack()) {
                        component.setHasBlindnessAttack(true);
                    }
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChancePoisonAttack()) {
                        component.setHasPoisonAttack(true);
                    }
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceSlownessAttack()) {
                        component.setHasSlownessAttack(true);
                    }
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceHungerAttack()) {
                        component.setHasHungerAttack(true);
                    }
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceBurningAttack()) {
                        component.setHasBurningAttack(true);
                    }

                    /*log.info("tracked bounty mob={}", component);
                    log.info("player info={}", SpoornBountyMobsUtil.getPlayerDataComponent(player));*/

                    // Sync entity data to client
                    SpoornBountyEntityRegistry.HOSTILE_ENTITY_DATA.sync(hostileEntity);

                    // This will trigger our EntityMixin which sets entity dimensions on the server side
                    hostileEntity.calculateDimensions();

                    // Heal entity to max health, triggers LivingEntityMixin with overridden health
                    hostileEntity.setHealth(hostileEntity.getMaxHealth());

                    // Set status effects
                    int glowDuration = ModConfig.get().bountyMobPermanentGlow ? Integer.MAX_VALUE
                            : ModConfig.get().bountyMobGlowDuration * 20;
                    hostileEntity.addStatusEffect(getStatusEffectInstance(StatusEffects.GLOWING, glowDuration, 0));

                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceResistance()) {
                        hostileEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstanceMaxDuration(StatusEffects.RESISTANCE, 1));
                    }
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceRegeneration()) {
                        hostileEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstanceMaxDuration(StatusEffects.REGENERATION, 1));
                    }
                    if (SpoornBountyMobsUtil.RANDOM.nextFloat() < tier.getChanceSpeed()) {
                        hostileEntity.addStatusEffect(SpoornBountyMobsUtil.getStatusEffectInstanceMaxDuration(StatusEffects.SPEED, 1));
                    }

                    try {
                        if (ModConfig.get().broadcastMessageWhenBountySpawned) {
                            String playerName = player.getDisplayName().getString();
                            List<String> broadcastDisabled = ModConfig.get().broadcastDisabled;
                            if (!broadcastDisabled.contains(playerName) && !broadcastDisabled.contains(Registries.ENTITY_TYPE.getId(hostileEntity.getType()).toString())) {
                                MutableText playerpart = Text.literal(playerName).formatted(Formatting.DARK_AQUA);
                                MutableText tierpart = Text.literal(tier.getTierType().getName()).formatted(tier.getTierType().getFormattings());
                                MutableText mobpart = Text.translatable(hostileEntity.getDisplayName().getString()).formatted(Formatting.DARK_GREEN);
                                player.getServer().getPlayerManager().broadcast(playerpart.append(BROADCAST_1).append(tierpart).append(BROADCAST_2).append(mobpart), false);
                            }
                        }
                    } catch (Exception e) {
                        log.error("Exception while trying to broadcast message for SpoornBountyMobs", e);
                    }
                }

                // Set entity as tracked
                component.track();
            }
        });
    }
}
