package org.spoorn.spoornbountymobs.config.tiers;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.spoorn.spoornbountymobs.config.Drop;

import java.util.ArrayList;
import java.util.List;

public class LegendaryTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 3]")
    public int spawnChance = 3;

    @Comment("This tier's Mob size scale [default = 2.7]")
    public double mobSizeScale = 2.7;

    @Comment("This tier's mob outline color in Decimal format [default = 32093]")
    public int glowColor = 32093;

    @Comment("Base bonus health for this tier [default = 60]")
    public int baseBonusHealth = 60;

    @Comment("Experience scale for this tier [default = 15]")
    public int experienceScale = 15;

    @Comment("Health increase per bounty score milestone [default = 25]")
    public int milestoneHealthIncrease = 25;

    @Comment("Damage increase per player bounty score level [default = 0.5]")
    public double milestoneDamageIncrease = 0.5;

    @Comment("Drops data.  See COMMON_TIER's config for example configuration schema and documentation.")
    public List<Drop> drops = new ArrayList<>();
}
