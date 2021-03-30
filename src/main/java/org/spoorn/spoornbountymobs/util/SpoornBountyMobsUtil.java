package org.spoorn.spoornbountymobs.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.EntityDataComponent;
import org.spoorn.spoornbountymobs.entity.PlayerDataComponent;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;

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

    public static PlayerDataComponent getPlayerDataComponent(PlayerEntity player) {
        return SpoornBountyEntityRegistry.PLAYER_DATA.get(player);
    }

    public static StatusEffectInstance getStatusEffectInstance(StatusEffect statusEffect, int duration, int amplifier) {
        return new StatusEffectInstance(statusEffect, duration, amplifier, false, false);
    }

    public static StatusEffectInstance getStatusEffectInstanceMaxDuration(StatusEffect statusEffect, int amplifier) {
        return new StatusEffectInstance(statusEffect, Integer.MAX_VALUE, amplifier, false, false);
    }

    /**
     * Get mob health increase based on entity's tier and player's bounty score.
     */
    public static float getHealthIncreaseFromBountyScore(PlayerEntity player, Entity entity) {
        EntityDataComponent entityDataComponent = getSpoornEntityDataComponent(entity);

        // No increase if mob doesn't have a bounty
        if (!entityDataComponent.hasBounty()) {
            return 0;
        }

        int bountyTier = getBountyHunterTier(player);
        switch (entityDataComponent.getSpoornBountyTier().getTierType()) {
            case COMMON_TIER:
                return bountyTier * ModConfig.get().COMMON_TIER.milestoneHealthIncrease;
            case UNCOMMON_TIER:
                return bountyTier * ModConfig.get().UNCOMMON_TIER.milestoneHealthIncrease;
            case RARE_TIER:
                return bountyTier * ModConfig.get().RARE_TIER.milestoneHealthIncrease;
            case EPIC_TIER:
                return bountyTier * ModConfig.get().EPIC_TIER.milestoneHealthIncrease;
            case LEGENDARY_TIER:
                return bountyTier * ModConfig.get().LEGENDARY_TIER.milestoneHealthIncrease;
            case DOOM_TIER:
                return bountyTier * ModConfig.get().DOOM_TIER.milestoneHealthIncrease;
            default:
                return 0;
        }
    }

    /**
     * Player's bounty hunter tier used to determine milestones in bounty hunting.
     */
    private static int getBountyHunterTier(PlayerEntity player) {
        return (int)calculateBountyScore(player)/ModConfig.get().playerBountyHunterTierIntervals;
    }

    /**
     * Bounty score calculated from weighted sum of tier kill counts.
     */
    private static double calculateBountyScore(PlayerEntity player) {
        return getPlayerDataComponent(player).getBountyScore();
    }
}
