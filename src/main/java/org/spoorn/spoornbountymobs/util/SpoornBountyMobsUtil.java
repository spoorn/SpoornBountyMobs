package org.spoorn.spoornbountymobs.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.spoorn.spoornbountymobs.SpoornBountyTier;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.entity.EntityDataComponent;

import java.util.Random;

public class SpoornBountyMobsUtil {

    public static final Random RANDOM = new Random();
    public static final EnumeratedDistribution<SpoornBountyTier> SPOORN_BOUNTY_TIERS = new EnumeratedDistribution(
        ImmutableList.of(
            new Pair(SpoornBountyTier.COMMON, SpoornBountyTier.COMMON.getWeight()),
            new Pair(SpoornBountyTier.UNCOMMON, SpoornBountyTier.UNCOMMON.getWeight()),
            new Pair(SpoornBountyTier.RARE, SpoornBountyTier.RARE.getWeight()),
            new Pair(SpoornBountyTier.EPIC, SpoornBountyTier.EPIC.getWeight()),
            new Pair(SpoornBountyTier.LEGENDARY, SpoornBountyTier.LEGENDARY.getWeight()),
            new Pair(SpoornBountyTier.DOOM, SpoornBountyTier.DOOM.getWeight())
    ));

    public static boolean isHostileEntity(Entity entity) {
        return entity instanceof HostileEntity;
    }

    public static boolean entityIsHostileAndHasBounty(Entity entity) {
        return entity instanceof HostileEntity && SpoornBountyEntityRegistry.HOSTILE_ENTITY_DATA.get(entity).hasBounty();
    }

    public static EntityDataComponent getSpoornEntityDataComponent(Entity entity) {
        return SpoornBountyEntityRegistry.HOSTILE_ENTITY_DATA.get(entity);
    }

    public static StatusEffectInstance getStatusEffectInstance(StatusEffect statusEffect, int duration, int amplifier) {
        return new StatusEffectInstance(statusEffect, duration, amplifier, false, false);
    }

    public static StatusEffectInstance getStatusEffectInstanceMaxDuration(StatusEffect statusEffect, int amplifier) {
        return new StatusEffectInstance(statusEffect, Integer.MAX_VALUE, amplifier, false, false);
    }
}
