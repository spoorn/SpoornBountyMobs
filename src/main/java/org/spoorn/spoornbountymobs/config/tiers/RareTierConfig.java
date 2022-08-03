package org.spoorn.spoornbountymobs.config.tiers;

import draylar.omegaconfig.api.Comment;
import org.spoorn.spoornbountymobs.config.Drop;
import org.spoorn.spoornbountymobs.config.WeightedItem;

import java.util.ArrayList;
import java.util.List;

public class RareTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 10]")
    public int spawnChance = 10;

    @Comment("This tier's Mob size scale [default = 2.0]")
    public double mobSizeScale = 2.0;

    @Comment("This tier's mob outline color in Decimal format [default = 3179]")
    public int glowColor = 3179;

    @Comment("Base bonus health for this tier [default = 40]")
    public int baseBonusHealth = 40;

    @Comment("Experience scale for this tier [default = 9]")
    public int experienceScale = 9;

    @Comment("Health increase per bounty score milestone [default = 15]")
    public int milestoneHealthIncrease = 15;

    @Comment("Damage increase per player bounty score level [default = 0.3]")
    public double milestoneDamageIncrease = 0.3;

    @Comment("Drops data.  See COMMON_TIER's config for example configuration schema and documentation.")
    public List<Drop> drops = new ArrayList<>(List.of(
            new Drop(".*", 1.0, 2, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:diamond", 1),
                    new WeightedItem("minecraft:emerald", 1),
                    new WeightedItem("minecraft:.*_(sword|axe|shovel|pickaxe|hoe|helmet|chestplate|leggings|boots)", 1),
                    new WeightedItem("minecraft:.*ingot", 1)
            ))),
            new Drop(".*", 1.0, 3, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:.*golden_apple", 10),
                    new WeightedItem("minecraft:golden_carrot", 10),
                    new WeightedItem("minecraft:cake", 1)
            ))),
            new Drop(".*", 0.5, 2, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:ender_pearl", 1),
                    new WeightedItem("minecraft:firework_rocket", 1)
            )))
    ));
}
