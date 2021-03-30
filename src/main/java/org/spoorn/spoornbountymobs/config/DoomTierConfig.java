package org.spoorn.spoornbountymobs.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class DoomTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 1]")
    public int spawnChance = 1;

    @Comment("Base bonus health for this tier [default = 70]")
    public int baseBonusHealth = 70;

    @Comment("Health increase per bounty score milestone [default = 30]")
    public int milestoneHealthIncrease = 30;
}
