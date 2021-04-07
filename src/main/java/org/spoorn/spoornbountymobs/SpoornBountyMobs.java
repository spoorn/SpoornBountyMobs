package org.spoorn.spoornbountymobs;

import net.fabricmc.api.ModInitializer;
import org.spoorn.spoornbountymobs.command.CommandRegistry;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

public class SpoornBountyMobs implements ModInitializer {

    public static final String MODID = "spoornbountymobs";

    @Override
    public void onInitialize() {
        // Config
        ModConfig.init();

        // Lazy initialize Bounty Tiers
        SpoornBountyTier.init();

        // Bounty registry
        SpoornBountyEntityRegistry.init();

        // Commands registry
        CommandRegistry.init();
    }
}
