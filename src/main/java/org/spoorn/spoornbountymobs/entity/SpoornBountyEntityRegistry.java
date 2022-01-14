package org.spoorn.spoornbountymobs.entity;

import static org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil.getStatusEffectInstance;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import lombok.extern.log4j.Log4j2;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

import java.util.UUID;

/**
 * Bounty Entity registry.  All things related to registering Bounties on Entities when player starts tracking one.
 */
@Log4j2
public class SpoornBountyEntityRegistry implements EntityComponentInitializer {

    private static final MutableText BROADCAST_1 = new TranslatableText("sbm.broadcast.part1").formatted(Formatting.WHITE);
    private static final MutableText BROADCAST_2 = new TranslatableText("sbm.broadcast.part2").formatted(Formatting.WHITE);

    // Hostile Entity data
    public static final ComponentKey<EntityDataComponent> HOSTILE_ENTITY_DATA =
            ComponentRegistryV3.INSTANCE.getOrCreate(EntityDataComponent.ID, EntityDataComponent.class);
    // Player data
    public static final ComponentKey<PlayerDataComponent> PLAYER_DATA =
            ComponentRegistryV3.INSTANCE.getOrCreate(PlayerDataComponent.ID, PlayerDataComponent.class);

    public static void init() {
        registerStartTrackingCallback();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Entity data
        registry.registerFor(HostileEntity.class, HOSTILE_ENTITY_DATA, e -> new SpoornBountyHostileEntityDataComponent(e));

        // Player data, should be persisted across sessions
        registry.registerForPlayers(PLAYER_DATA, player -> new SpoornBountyPlayerDataComponent(player), RespawnCopyStrategy.ALWAYS_COPY);
    }

    // On entity being tracked by a player, chance to mark the entity as having a bounty.  Update entity data
    private static void registerStartTrackingCallback() {
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            double spawnChance = ModConfig.get().bountySpawnChance;
            if (spawnChance > 0 && SpoornBountyMobsUtil.isHostileEntity(trackedEntity)) {
                HostileEntity hostileEntity = (HostileEntity) trackedEntity;
                EntityDataComponent component =
                        SpoornBountyMobsUtil.getSpoornEntityDataComponent(hostileEntity);
                if (!component.hasTracked() && (SpoornBountyMobsUtil.RANDOM.nextFloat() < (1.0/ ModConfig.get().bountySpawnChance))) {
                    // Set Entity data
                    component.setHasBounty(true);
                    component.setBonusBountyTierHealth(SpoornBountyMobsUtil.getHealthIncreaseFromBountyScore(player, hostileEntity));

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
                        MutableText playerpart = new LiteralText(player.getDisplayName().getString()).formatted(Formatting.DARK_AQUA);
                        MutableText tierpart = new LiteralText(tier.getTierType().getName()).formatted(tier.getTierType().getFormattings());
                        MutableText mobpart = new LiteralText(hostileEntity.getDisplayName().getString()).formatted(Formatting.DARK_GREEN);
                        player.getServer().getPlayerManager().broadcastChatMessage(playerpart.append(BROADCAST_1).append(tierpart).append(BROADCAST_2).append(mobpart), MessageType.CHAT, Util.NIL_UUID);
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
