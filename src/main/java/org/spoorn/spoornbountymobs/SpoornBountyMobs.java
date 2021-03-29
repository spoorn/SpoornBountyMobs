package org.spoorn.spoornbountymobs;

import net.fabricmc.api.ModInitializer;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.SpoornBountyEntityRegistry;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;

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
    }
}
