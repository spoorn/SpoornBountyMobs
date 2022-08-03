package org.spoorn.spoornbountymobs.config.tiers;

import draylar.omegaconfig.api.Comment;
import org.spoorn.spoornbountymobs.config.Drop;
import org.spoorn.spoornbountymobs.config.WeightedItem;

import java.util.ArrayList;
import java.util.List;

public class EpicTierConfig {

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 6]")
    public int spawnChance = 6;

    @Comment("This tier's Mob size scale [default = 2.3]")
    public double mobSizeScale = 2.3;

    @Comment("This tier's mob outline color in Decimal format [default = 2949204]")
    public int glowColor = 2949204;

    @Comment("Base bonus health for this tier [default = 50]")
    public int baseBonusHealth = 50;

    @Comment("Experience scale for this tier [default = 12]")
    public int experienceScale = 12;

    @Comment("Health increase per bounty score milestone [default = 20]")
    public int milestoneHealthIncrease = 20;

    @Comment("Damage increase per player bounty score level [default = 0.4]")
    public double milestoneDamageIncrease = 0.4;

    @Comment("Drops data.  See COMMON_TIER's config for example configuration schema and documentation.")
    public List<Drop> drops = new ArrayList<>(List.of(
            new Drop(".*", 1.0, 2, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:diamond.*", 1),
                    new WeightedItem("minecraft:emerald_block", 1),
                    new WeightedItem("minecraft:netherite.*", 1),
                    new WeightedItem("minecraft:.*_(sword|axe|shovel|pickaxe|hoe|helmet|chestplate|leggings|boots)", 2)
            ))),
            new Drop(".*", 1.0, 4, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:.*golden_apple", 10),
                    new WeightedItem("minecraft:golden_carrot", 10),
                    new WeightedItem("minecraft:cake", 1)
            ))),
            new Drop(".*", 0.5, 4, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:ender_pearl", 1),
                    new WeightedItem("minecraft:firework_rocket", 1)
            )))
    ));
}
