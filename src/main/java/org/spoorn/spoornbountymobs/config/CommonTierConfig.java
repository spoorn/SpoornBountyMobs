package org.spoorn.spoornbountymobs.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

public class CommonTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 20]")
    public int spawnChance = 20;

    @Comment("Base bonus health for this tier [default = 20]")
    public int baseBonusHealth = 20;

    @Comment("Experience scale for this tier [default = 3]")
    public int experienceScale = 3;

    @Comment("Health increase per bounty score milestone [default = 5]")
    public int milestoneHealthIncrease = 5;

    @Comment("Damage increase per bounty score milestone [default = 0.2]")
    public double milestoneDamageIncrease = 0.2;
}
