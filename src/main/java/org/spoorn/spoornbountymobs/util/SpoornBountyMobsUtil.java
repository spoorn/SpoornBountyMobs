package org.spoorn.spoornbountymobs.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;

public class SpoornBountyMobsUtil {

    public static boolean isHostileEntity(Entity entity) {
        return entity instanceof HostileEntity;
    }
}
