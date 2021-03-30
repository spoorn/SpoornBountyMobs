package org.spoorn.spoornbountymobs.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class LegendaryTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 3]")
    public int spawnChance = 3;

    @Comment("Base bonus health for this tier [default = 60]")
    public int baseBonusHealth = 60;

    @Comment("Health increase per bounty score milestone [default = 25]")
    public int milestoneHealthIncrease = 25;
}
