package org.spoorn.spoornbountymobs;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Bounty tiers to make bounties more dynamic.
 */
@EqualsAndHashCode
@ToString
public final class SpoornBountyTier {

    public static final SpoornBountyTier COMMON = new SpoornBountyTier("common", 20, 2f, 3, 0, 1);
    public static final SpoornBountyTier UNCOMMON = new SpoornBountyTier("uncommon", 15, 3f, 6, 1, 2);
    public static final SpoornBountyTier RARE = new SpoornBountyTier("rare", 10, 4f, 9, 2, 3);
    public static final SpoornBountyTier EPIC = new SpoornBountyTier("epic", 6, 5f, 12, 3, 4);
    public static final SpoornBountyTier LEGENDARY = new SpoornBountyTier("legendary", 3, 6f, 15, 4, 5);
    public static final SpoornBountyTier DOOM = new SpoornBountyTier("doom", 1, 7f, 20, 5, 6);

    @Getter
    private String value;
    @Getter
    private double weight;
    @Getter
    private float maxHealthScale;
    @Getter
    private int experienceScale;
    @Getter
    private int minDamageIncrease;
    @Getter
    private int maxDamageIncrease;

    private SpoornBountyTier(String value, int weight, float maxHealthScale, int experienceScale,
                             int minDamageIncrease, int maxDamageIncrease) {
        this.value = value;
        this.weight = weight;
        this.maxHealthScale = maxHealthScale;
        this.experienceScale = experienceScale;
        this.minDamageIncrease = minDamageIncrease;
        this.maxDamageIncrease = maxDamageIncrease;
    }

    public static SpoornBountyTier fromValue(String value) {
        switch (value) {
            case "common":
                return COMMON;
            case "uncommon":
                return UNCOMMON;
            case "rare":
                return RARE;
            case "epic":
                return EPIC;
            case "legendary":
                return LEGENDARY;
            case "doom":
                return DOOM;
            default:
                return COMMON;
        }
    }
}
