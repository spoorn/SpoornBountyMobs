package org.spoorn.spoornbountymobs.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.spoorn.spoornbountymobs.SpoornBountyMobs;

@Config(name = SpoornBountyMobs.MODID)
public class ModConfig implements ConfigData {

    @Comment("Chance for mob to spawn with a bounty [1/value] [default = 300]")
    public int bountySpawnChance = 300;

    @Comment("Bounty mob size scale [default = 2.0]")
    public double bountyMobSizeScale = 2.0;

    @Comment("True if Bounty mobs should glow through walls permanently, else false [default = false]")
    public boolean bountyMobPermanentGlow = false;

    @Comment("Duration in seconds Bounty mobs should glow when they are first found if bountyMobPermanentGlow is false [default = 15]")
    public int bountyMobGlowDuration = 15;

    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
