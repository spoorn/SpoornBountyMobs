package org.spoorn.spoornbountymobs.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.spoorn.spoornbountymobs.SpoornBountyMobs;

@Config(name = SpoornBountyMobs.MODID)
public class ModConfig implements ConfigData {

    @Comment("Chance for mob to spawn with a bounty [1/value] [default = 100]")
    public int bountySpawnChance = 100;

    @Comment("True if Bounty mobs should glow through walls permanently, else false [default = false]")
    public boolean bountyMobPermanentGlow = false;

    @Comment("Duration in seconds Bounty mobs should glow when they are first found if bountyMobPermanentGlow is false [default = 20]")
    public int bountyMobGlowDuration = 20;

    @Comment("Player's Bounty Hunter tier increments at this value [default = 10]")
    public int playerBountyHunterTierIntervals = 10;

    @Comment("Bonus health for Player per Bounty Hunter tier [default = 2 (one heart)]")
    @ConfigEntry.BoundedDiscrete(min=0, max=Long.MAX_VALUE)
    public double playerBonusHealthPerBountyHunterTier = 2;

    @Comment("Bonus damage for Player per Bounty Hunter tier [default = 0.5]")
    @ConfigEntry.BoundedDiscrete(min=0, max=Long.MAX_VALUE)
    public double playerBonusDamagePerBountyHunterTier = 0.5;

    @Comment("Percentage of player's Bounty score LOST on death [default = 20, min=0, max=100]")
    @ConfigEntry.BoundedDiscrete(min=0, max=100)
    public double playerDeathBountyScorePenalty = 20;

    @Comment("Common tier bounty mobs")
    @ConfigEntry.Gui.CollapsibleObject
    public CommonTierConfig COMMON_TIER = new CommonTierConfig();

    @Comment("Uncommon tier bounty mobs")
    @ConfigEntry.Gui.CollapsibleObject
    public UncommonTierConfig UNCOMMON_TIER = new UncommonTierConfig();

    @Comment("Rare tier bounty mobs")
    @ConfigEntry.Gui.CollapsibleObject
    public RareTierConfig RARE_TIER = new RareTierConfig();

    @Comment("Epic tier bounty mobs")
    @ConfigEntry.Gui.CollapsibleObject
    public EpicTierConfig EPIC_TIER = new EpicTierConfig();

    @Comment("Legendary tier bounty mobs")
    @ConfigEntry.Gui.CollapsibleObject
    public LegendaryTierConfig LEGENDARY_TIER = new LegendaryTierConfig();

    @Comment("Doom tier bounty mobs")
    @ConfigEntry.Gui.CollapsibleObject
    public DoomTierConfig DOOM_TIER = new DoomTierConfig();

    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
