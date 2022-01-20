package org.spoorn.spoornbountymobs.config.tiers;

import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.spoorn.spoornbountymobs.config.Drop;

import java.util.ArrayList;
import java.util.List;

public class CommonTierConfig {

    private static final String EXAMPLE_LOOT_JSON = "Here you can configure what loot drops from bounty mobs.  See comments below for information on each data field.\n" +
            "This works for any modded mobs or items as well, as long as you get the identifier regexes right!\n" +
            "If any identifiers are missing or do not exist in the game, it will simply be skipped and fail safely.\n" +
            "If any of the mob identifiers conflict (e.g. you specify a \"minecraft:creeper\" entry, but also a wildcard default \".*\" entry,\n" +
            "only the first matched drop data in the drops list will be applied.\n" +
            "Example:\n\n" +
            "\"drops\": [\n" +
            "\t{\n" +
            "\t\t\"entityId\": \"minecraft:creeper\",  // Entity identifier this drop configuration is for\n" +
            "\t\t\"dropChance\": 0.5,  // Overall chance for each roll to drop an item\n" +
            "\t\t\"rolls\": 2,  // Number of times to roll for a drop.  This allows for multiple drops.\n" +
            "\t\t// All the items that can drop from this entity\n" +
            "\t\t\"items\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t// Item's minecraft identifier.  This can be another mod's identifier.\n" +
            "\t\t\t\t\"item\": \"minecraft:string\",\n" +
            "\t\t\t\t// Weight for this item.  Higher number means higher chance to drop this item relative to others for each roll.\n" +
            "\t\t\t\t\"weight\": 3\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"item\": \"minecraft:iron_sword\",\n" +
            "\t\t\t\t\"weight\": 4\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"item\": \"minecraft:gunpowder\",\n" +
            "\t\t\t\t\"weight\": 1\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"entityId\": \"exampleModId:.*\",  // EntityId can be a regex, so you can add other mod's entities\n" +
            "\t\t\"dropChance\": 0.9,\n" +
            "\t\t\"rolls\": 1,\n" +
            "\t\t\"items\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"item\": \"minecraft:.*sword\",  // Item drops can use regex as well.  This drops any sword.\n" +
            "\t\t\t\t\"weight\": 1\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"entityId\": \".*\", // You can use regex for the mob identifier.  \".*\" is a wildcard and matches ALL mobs.\n" +
            "\t\t\"dropChance\": 0.5,\n" +
            "\t\t\"rolls\": 1,\n" +
            "\t\t\"items\": [\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"item\": \"minecraft:diamond\",\n" +
            "\t\t\t\t\"weight\": 3\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"item\": \"minecraft:ender_pearl\",\n" +
            "\t\t\t\t\"weight\": 4\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "]";

    @Comment("Spawn weight of this tier (higher number is higher chance of spawn) [default = 20]")
    public int spawnChance = 20;

    @Comment("This tier's Mob size scale [default = 1.5]")
    public double mobSizeScale = 1.5;

    @Comment("This tier's mob outline color in Decimal format [default = 20228]")
    public int glowColor = 20228;

    @Comment("Base bonus health for this tier [default = 20]")
    public int baseBonusHealth = 20;

    @Comment("Experience scale for this tier [default = 3]")
    public int experienceScale = 3;

    @Comment("Health increase per bounty score milestone [default = 5]")
    public int milestoneHealthIncrease = 5;

    @Comment("Damage increase per player bounty score level [default = 0.2]")
    public double milestoneDamageIncrease = 0.2;

    // WARNING: Setting defaults may override set configurations in the json file.  See https://github.com/shedaniel/cloth-config/issues/104.
    @Comment(EXAMPLE_LOOT_JSON)
    public List<Drop> drops = new ArrayList<>();
}
