package org.spoorn.spoornbountymobs.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class RareTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 10]")
    public int spawnChance = 10;

    @Comment("Base bonus health for this tier [default = 40]")
    public int baseBonusHealth = 40;

    @Comment("Health increase per bounty score milestone [default = 15]")
    public int milestoneHealthIncrease = 15;
}
