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

    public static SpoornBountyTier COMMON;
    public static SpoornBountyTier UNCOMMON;
    public static SpoornBountyTier RARE;
    public static SpoornBountyTier EPIC;
    public static SpoornBountyTier LEGENDARY;
    public static SpoornBountyTier DOOM;

    @Getter
    private String value;
    @Getter
    private double weight;
    @Getter
    private float maxHealthIncrease;
    @Getter
    private int experienceScale;
    @Getter
    private int minDamageIncrease;
    @Getter
    private int maxDamageIncrease;

    private SpoornBountyTier(String value, int weight, float maxHealthIncrease, int experienceScale,
                             int minDamageIncrease, int maxDamageIncrease) {
        this.value = value;
        this.weight = weight;
        this.maxHealthIncrease = maxHealthIncrease;
        this.experienceScale = experienceScale;
        this.minDamageIncrease = minDamageIncrease;
        this.maxDamageIncrease = maxDamageIncrease;
    }

    // Lazy initialization of static values so that we can wait for ModConfig to get initialized first
    public static void init() {
        COMMON = new SpoornBountyTier("common", 20, 80, 3, 0, 1);
        UNCOMMON = new SpoornBountyTier("uncommon", 15, 120, 6, 1, 2);
        RARE = new SpoornBountyTier("rare", 10, 240, 9, 2, 3);
        EPIC = new SpoornBountyTier("epic", 6, 360, 12, 3, 4);
        LEGENDARY = new SpoornBountyTier("legendary", 3, 450, 15, 4, 5);
        DOOM = new SpoornBountyTier("doom", 1, 600, 20, 5, 6);
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
