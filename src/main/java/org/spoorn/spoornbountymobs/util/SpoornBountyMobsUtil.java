package org.spoorn.spoornbountymobs.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;

import java.util.Random;

public class SpoornBountyMobsUtil {

    public static final Random RANDOM = new Random();

    public static boolean isHostileEntity(Entity entity) {
        return entity instanceof HostileEntity;
    }

    public static boolean entityHasBounty(Entity entity) {
        return SpoornBountyEntityRegistry.HOSTILE_ENTITY_DATA.get(entity).hasBounty();
    }
}
