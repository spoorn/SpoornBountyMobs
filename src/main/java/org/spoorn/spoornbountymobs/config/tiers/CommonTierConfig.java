package org.spoorn.spoornbountymobs.config.tiers;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class CommonTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 20]")
    public int spawnChance = 20;

    @Comment("This tier's Mob size scale [default = 1.5]")
    public double mobSizeScale = 1.5;

    @Comment("This tier's mob outline color in Decimal format [default = 20228]")
    public int glowColor = 20228;

    @Comment("Base bonus health for this tier [default = 20]")
    public int baseBonusHealth = 20;

    @Comment("Experience scale for this tier [default = 3]")
    public int experienceScale = 3;

    @Comment("Health increase per bounty score milestone [default = 5]")
    public int milestoneHealthIncrease = 5;

    @Comment("Damage increase per player bounty score level [default = 0.2]")
    public double milestoneDamageIncrease = 0.2;
}
