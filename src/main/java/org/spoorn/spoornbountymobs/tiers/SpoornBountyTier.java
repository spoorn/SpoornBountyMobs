package org.spoorn.spoornbountymobs.tiers;

import static org.spoorn.spoornbountymobs.tiers.SpoornBountyTierTypes.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Bounty tiers to make bounties more dynamic.
 */
@EqualsAndHashCode
@ToString
public final class SpoornBountyTier {

    public static SpoornBountyTier COMMON;
    public static SpoornBountyTier UNCOMMON;
    public static SpoornBountyTier RARE;
    public static SpoornBountyTier EPIC;
    public static SpoornBountyTier LEGENDARY;
    public static SpoornBountyTier DOOM;

    @Getter
    private SpoornBountyTierTypes tierType;
    @Getter
    private double weight;
    @Getter
    private double bountyScoreScale;
    @Getter
    private float maxHealthIncrease;
    @Getter
    private int experienceScale;
    @Getter
    private int minDamageIncrease;
    @Getter
    private int maxDamageIncrease;

    private SpoornBountyTier(SpoornBountyTierTypes tierType, int weight, double bountyScoreScale,
        float maxHealthIncrease, int experienceScale, int minDamageIncrease, int maxDamageIncrease) {
        this.tierType = tierType;
        this.weight = weight;
        this.bountyScoreScale = bountyScoreScale;
        this.maxHealthIncrease = maxHealthIncrease;
        this.experienceScale = experienceScale;
        this.minDamageIncrease = minDamageIncrease;
        this.maxDamageIncrease = maxDamageIncrease;
    }

    // Lazy initialization of static values so that we can wait for ModConfig to get initialized first
    public static void init() {
        COMMON = new SpoornBountyTier(COMMON_TIER, 20, 1, 80, 3, 0, 1);
        UNCOMMON = new SpoornBountyTier(UNCOMMON_TIER, 15, 2, 120, 6, 1, 2);
        RARE = new SpoornBountyTier(RARE_TIER, 10, 4, 240, 9, 2, 3);
        EPIC = new SpoornBountyTier(EPIC_TIER, 6, 5, 360, 12, 3, 4);
        LEGENDARY = new SpoornBountyTier(LEGENDARY_TIER, 3, 7, 450, 15, 4, 5);
        DOOM = new SpoornBountyTier(DOOM_TIER, 1, 8, 600, 20, 5, 6);
    }

    public static SpoornBountyTier fromValue(SpoornBountyTierTypes spoornBountyTierTypes) {
        switch (spoornBountyTierTypes) {
            case COMMON_TIER:
                return COMMON;
            case UNCOMMON_TIER:
                return UNCOMMON;
            case RARE_TIER:
                return RARE;
            case EPIC_TIER:
                return EPIC;
            case LEGENDARY_TIER:
                return LEGENDARY;
            case DOOM_TIER:
                return DOOM;
            default:
                return COMMON;
        }
    }
}
