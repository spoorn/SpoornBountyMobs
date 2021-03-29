package org.spoorn.spoornbountymobs.entity;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
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
    public static final ComponentKey<SpoornBountyHostileEntityDataComponent> HOSTILE_ENTITY_DATA =
            ComponentRegistryV3.INSTANCE.getOrCreate(SpoornBountyHostileEntityDataComponent.ID,
                SpoornBountyHostileEntityDataComponent.class);

    public static void init() {
        registerStartTrackingCallback();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(HostileEntity.class, HOSTILE_ENTITY_DATA, e -> new SpoornBountyHostileEntityDataComponent(e));
    }

    // On entity being tracked by a player, chance to mark the entity as having a bounty.  Update entity data
    private static void registerStartTrackingCallback() {
        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            if (SpoornBountyMobsUtil.isHostileEntity(trackedEntity)) {
                float randFloat = SpoornBountyMobsUtil.RANDOM.nextFloat();
                if (randFloat < (1.0/ ModConfig.get().bountySpawnChance)) {
                    HostileEntity hostileEntity = (HostileEntity) trackedEntity;
                    SpoornBountyHostileEntityDataComponent component =
                        SpoornBountyEntityRegistry.HOSTILE_ENTITY_DATA.get(hostileEntity);

                    // Set Entity data
                    component.setHasBounty(true);
                    component.setSpoornBountyTier(SpoornBountyMobsUtil.SPOORN_BOUNTY_TIERS.sample());

                    log.info("tracked component={}", component);

                    SpoornBountyEntityRegistry.HOSTILE_ENTITY_DATA.sync(hostileEntity);

                    // This will trigger our EntityMixin which sets entity dimensions on the server side
                    hostileEntity.calculateDimensions();
                    if (ModConfig.get().bountyMobGlow) {
                        hostileEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, Integer.MAX_VALUE, 0, false, false));
                    }
                }
            }
        });
    }
}
