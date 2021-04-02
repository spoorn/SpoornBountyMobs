package org.spoorn.spoornbountymobs.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class EpicTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 6]")
    public int spawnChance = 6;

    @Comment("This tier's Mob size scale [default = 2.3]")
    public double mobSizeScale = 2.3;

    @Comment("Base bonus health for this tier [default = 50]")
    public int baseBonusHealth = 50;

    @Comment("Experience scale for this tier [default = 12]")
    public int experienceScale = 12;

    @Comment("Health increase per bounty score milestone [default = 20]")
    public int milestoneHealthIncrease = 20;

    @Comment("Damage increase per bounty score milestone [default = 0.4]")
    public double milestoneDamageIncrease = 0.4;
}
