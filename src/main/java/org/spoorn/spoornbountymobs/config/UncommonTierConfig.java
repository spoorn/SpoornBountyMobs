package org.spoorn.spoornbountymobs.config;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.spoorn.spoornbountymobs.config.Drop;

import java.util.ArrayList;
import java.util.List;

public class UncommonTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 15]")
    public int spawnChance = 15;

    @Comment("This tier's Mob size scale [default = 1.75]")
    public double mobSizeScale = 1.75;

    @Comment("This tier's mob outline color in Decimal format [default = 8729344]")
    public int glowColor = 8729344;

    @Comment("Base bonus health for this tier [default = 30]")
    public int baseBonusHealth = 30;

    @Comment("Experience scale for this tier [default = 6]")
    public int experienceScale = 6;

    @Comment("Health increase per bounty score milestone [default = 10]")
    public int milestoneHealthIncrease = 10;

    @Comment("Damage increase per bounty score milestone [default = 0.2]")
    public double milestoneDamageIncrease = 0.2;

    @Comment("Drops data.  See COMMON_TIER's config for example configuration schema and documentation.")
    public List<Drop> drops = new ArrayList<>();
}
