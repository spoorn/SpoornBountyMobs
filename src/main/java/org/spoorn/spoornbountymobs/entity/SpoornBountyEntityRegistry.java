package org.spoorn.spoornbountymobs.entity;

import static org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil.getStatusEffectInstance;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import lombok.extern.log4j.Log4j2;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import org.spoorn.spoornbountymobs.config.ModConfig;
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
        registry.registerFor(HostileEntity.class, HOSTILE_ENTITY_DATA, e -> new SpoornBountyHostileEntityDataComponent(e));

        // Player data, should be persisted across sessions
        registry.registerForPlayers(PLAYER_DATA, player -> new SpoornBountyPlayerDataComponent(player), RespawnCopyStrategy.ALWAYS_COPY);
    }

    // On entity being tracked by a player, chance to mark the entity as having a bounty.  Update entity data
    private static void registerStartTrackingCallback() {
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            if (SpoornBountyMobsUtil.isHostileEntity(trackedEntity)) {
                HostileEntity hostileEntity = (HostileEntity) trackedEntity;
                EntityDataComponent component =
                        SpoornBountyMobsUtil.getSpoornEntityDataComponent(hostileEntity);
                if (!component.hasBounty() && (SpoornBountyMobsUtil.RANDOM.nextFloat() < (1.0/ ModConfig.get().bountySpawnChance))) {
                    // Set Entity data
                    component.setHasBounty(true);
                    component.setSpoornBountyTier(SpoornBountyMobsUtil.SPOORN_BOUNTY_TIERS.sample());
                    component.setBonusHealth(SpoornBountyMobsUtil.getHealthIncreaseFromBountyScore(player, hostileEntity));

                    //log.info("tracked bounty mob={}", component);

                    SpoornBountyEntityRegistry.HOSTILE_ENTITY_DATA.sync(hostileEntity);

                    // This will trigger our EntityMixin which sets entity dimensions on the server side
                    hostileEntity.calculateDimensions();

                    // Set status effects
                    int glowDuration = ModConfig.get().bountyMobPermanentGlow ? Integer.MAX_VALUE
                        : ModConfig.get().bountyMobGlowDuration * 20;
                    hostileEntity.addStatusEffect(getStatusEffectInstance(StatusEffects.GLOWING, glowDuration, 0));

                    // Heal entity to max health, triggers LivingEntityMixin with overridden health
                    hostileEntity.setHealth(hostileEntity.getMaxHealth());
                }
            }
        });
    }
}
