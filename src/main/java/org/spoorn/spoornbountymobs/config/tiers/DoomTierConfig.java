package org.spoorn.spoornbountymobs.config.tiers;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class DoomTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 1]")
    public int spawnChance = 1;

    @Comment("This tier's Mob size scale [default = 3.0]")
    public double mobSizeScale = 3.0;

    @Comment("This tier's mob outline color in Decimal format [default = 7012352]")
    public int glowColor = 7012352;

    @Comment("Base bonus health for this tier [default = 70]")
    public int baseBonusHealth = 70;

    @Comment("Experience scale for this tier [default = 20]")
    public int experienceScale = 20;

    @Comment("Health increase per bounty score milestone [default = 30]")
    public int milestoneHealthIncrease = 30;

    @Comment("Damage increase per player bounty score level [default = 0.6]")
    public double milestoneDamageIncrease = 0.6;
}
