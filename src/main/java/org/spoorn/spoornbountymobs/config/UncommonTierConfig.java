package org.spoorn.spoornbountymobs.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class UncommonTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 15]")
    public int spawnChance = 15;

    @Comment("Base bonus health for this tier [default = 30]")
    public int baseBonusHealth = 30;

    @Comment("Health increase per bounty score milestone [default = 10]")
    public int milestoneHealthIncrease = 10;
}
