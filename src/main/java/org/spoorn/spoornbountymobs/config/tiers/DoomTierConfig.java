package org.spoorn.spoornbountymobs.config.tiers;

import draylar.omegaconfig.api.Comment;
import org.spoorn.spoornbountymobs.config.Drop;
import org.spoorn.spoornbountymobs.config.WeightedItem;

import java.util.ArrayList;
import java.util.List;

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
    public List<Drop> drops = new ArrayList<>(List.of(
            new Drop(".*", 0.05, 1, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:nether_star", 1)
            ))),
            new Drop(".*", 1.0, 4, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:diamond.*", 2),
                    new WeightedItem("minecraft:emerald_block", 1),
                    new WeightedItem("minecraft:netherite.*", 1)
            ))),
            new Drop(".*", 0.1, 2, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:carved_pumpkin", 1),
                    new WeightedItem("minecraft:pumpkin_pie", 1),
                    new WeightedItem("minecraft:music_disc.*", 1)
            ))),
            new Drop(".*", 1.0, 4, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:.*golden_apple", 10),
                    new WeightedItem("minecraft:golden_carrot", 10),
                    new WeightedItem("minecraft:cake", 1)
            ))),
            new Drop(".*", 0.5, 8, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:ender_pearl", 1),
                    new WeightedItem("minecraft:firework_rocket", 1)
            ))),
            new Drop(".*", 0.05, 1, new ArrayList<>(List.of(
                    new WeightedItem("minecraft:elytra", 1),
                    new WeightedItem("minecraft:totem_of_undying", 1)
            )))
    ));
}
