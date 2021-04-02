package org.spoorn.spoornbountymobs.tiers;

import static org.spoorn.spoornbountymobs.tiers.SpoornBountyTierTypes.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.spoorn.spoornbountymobs.config.*;

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
    private float mobSizeScale;
    @Getter
    private double bountyScoreScale;
    @Getter
    private float maxBaseHealthIncrease;
    @Getter
    private int experienceScale;
    @Getter
    private double minBaseDamageIncrease;
    @Getter
    private double maxBaseDamageIncrease;
    @Getter
    private double milestoneDamageIncrease;

    private SpoornBountyTier(SpoornBountyTierTypes tierType, int weight, float mobSizeScale, double bountyScoreScale,
        float maxBaseHealthIncrease, int experienceScale, double minBaseDamageIncrease, double maxBaseDamageIncrease,
        double milestoneDamageIncrease) {
        this.tierType = tierType;
        this.weight = weight;
        this.mobSizeScale = mobSizeScale;
        this.bountyScoreScale = bountyScoreScale;
        this.maxBaseHealthIncrease = maxBaseHealthIncrease;
        this.experienceScale = experienceScale;
        this.minBaseDamageIncrease = minBaseDamageIncrease;
        this.maxBaseDamageIncrease = maxBaseDamageIncrease;
        this.milestoneDamageIncrease = milestoneDamageIncrease;
    }

    // Lazy initialization of static values so that we can wait for ModConfig to get initialized first
    public static void init() {
        CommonTierConfig commonConfig = ModConfig.get().COMMON_TIER;
        UncommonTierConfig uncommonConfig = ModConfig.get().UNCOMMON_TIER;
        RareTierConfig rareConfig = ModConfig.get().RARE_TIER;
        EpicTierConfig epicConfig = ModConfig.get().EPIC_TIER;
        LegendaryTierConfig legendaryConfig = ModConfig.get().LEGENDARY_TIER;
        DoomTierConfig doomConfig = ModConfig.get().DOOM_TIER;
        COMMON = new SpoornBountyTier(COMMON_TIER, commonConfig.spawnChance, (float)commonConfig.mobSizeScale, 1,
            commonConfig.baseBonusHealth, commonConfig.experienceScale, 0, 1, commonConfig.milestoneDamageIncrease);
        UNCOMMON = new SpoornBountyTier(UNCOMMON_TIER, uncommonConfig.spawnChance, (float)uncommonConfig.mobSizeScale, 2,
            uncommonConfig.baseBonusHealth, uncommonConfig.experienceScale, 1, 2, uncommonConfig.milestoneDamageIncrease);
        RARE = new SpoornBountyTier(RARE_TIER, rareConfig.spawnChance, (float)rareConfig.mobSizeScale, 4,
            rareConfig.baseBonusHealth, rareConfig.experienceScale, 2, 3, rareConfig.milestoneDamageIncrease);
        EPIC = new SpoornBountyTier(EPIC_TIER, epicConfig.spawnChance, (float)epicConfig.mobSizeScale, 5,
            epicConfig.baseBonusHealth, epicConfig.experienceScale, 3, 4, epicConfig.milestoneDamageIncrease);
        LEGENDARY = new SpoornBountyTier(LEGENDARY_TIER, legendaryConfig.spawnChance, (float)legendaryConfig.mobSizeScale,
            7, legendaryConfig.baseBonusHealth, legendaryConfig.experienceScale, 4, 5, legendaryConfig.milestoneDamageIncrease);
        DOOM = new SpoornBountyTier(DOOM_TIER, doomConfig.spawnChance, (float)doomConfig.mobSizeScale, 8,
            doomConfig.baseBonusHealth, doomConfig.experienceScale, 5, 6, doomConfig.milestoneDamageIncrease);
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
