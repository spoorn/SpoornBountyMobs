package org.spoorn.spoornbountymobs.entity;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Log4j2
public class SpoornBountyRegistry {

    public static void init() {
        // Do nothing but this is required because mod loader stupid and needs explicit code entrypoints to load for
        // client vs server statics

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            if (SpoornBountyMobsUtil.isHostileEntity(trackedEntity)) {
                HostileEntity hostileEntity = (HostileEntity) trackedEntity;
                //hostileEntity.applyStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 400, 1));
                //hostileEntity.calculateDimensions();
            }
        });
    }


}
