package org.spoorn.spoornbountymobs.config.tiers;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.spoorn.spoornbountymobs.config.Drop;
import org.spoorn.spoornbountymobs.config.WeightedItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Comment("Drops data.  See COMMON_TIER's config for example configuration schema and documentation.")
    public Map<String, Drop> drops = new HashMap<>(Map.ofEntries(
            Map.entry(".*", new Drop(0.5, 2, List.of(new WeightedItem("minecraft:diamond", 1))))
    ));
}
