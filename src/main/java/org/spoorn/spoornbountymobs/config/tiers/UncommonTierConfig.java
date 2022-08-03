package org.spoorn.spoornbountymobs.config.tiers;

import draylar.omegaconfig.api.Comment;
import org.spoorn.spoornbountymobs.config.Drop;
import org.spoorn.spoornbountymobs.config.WeightedItem;

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

    @Comment("Damage increase per player bounty score level [default = 0.2]")
    public double milestoneDamageIncrease = 0.2;

    @Comment("Drops data.  See COMMON_TIER's config for example configuration schema and documentation.")
    public List<Drop> drops = new ArrayList<>(List.of(
            new Drop(".*", 0.5, 2, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:diamond", 1),
                    new WeightedItem("minecraft:emerald", 1),
                    new WeightedItem("minecraft:.*_(sword|axe|shovel|pickaxe|hoe|helmet|chestplate|leggings|boots)", 1),
                    new WeightedItem("minecraft:.*ingot", 1)
            ))),
            new Drop(".*", 0.5, 3, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:.*golden_apple", 10),
                    new WeightedItem("minecraft:golden_carrot", 10),
                    new WeightedItem("minecraft:cake", 1)
            )))
    ));
}
