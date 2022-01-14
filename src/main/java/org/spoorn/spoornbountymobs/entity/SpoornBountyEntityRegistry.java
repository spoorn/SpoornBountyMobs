package org.spoorn.spoornbountymobs.entity;

import static org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil.getStatusEffectInstance;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.component.EntityDataComponent;
import org.spoorn.spoornbountymobs.entity.component.PlayerDataComponent;
import org.spoorn.spoornbountymobs.entity.component.SpoornBountyHostileEntityDataComponent;
import org.spoorn.spoornbountymobs.entity.component.SpoornBountyPlayerDataComponent;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

/**
 * Bounty Entity registry.  All things related to registering Bounties on Entities when player starts tracking one.
 */
@Log4j2
public class SpoornBountyEntityRegistry implements EntityComponentInitializer {

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
        registry.registerFor(HostileEntity.class, HOSTILE_ENTITY_DATA, SpoornBountyHostileEntityDataComponent::new);

        // Player data, should be persisted across sessions
        registry.registerForPlayers(PLAYER_DATA, SpoornBountyPlayerDataComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
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
                }

                // Set entity as tracked
                component.track();
            }
        });
    }
}
