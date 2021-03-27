package org.spoorn.spoornbountymobs;

import net.fabricmc.api.ModInitializer;
import org.spoorn.spoornbountymobs.config.ModConfig;
import org.spoorn.spoornbountymobs.entity.SpoornBountyRegistry;

public class SpoornBountyMobs implements ModInitializer {

    public static final String MODID = "spoornbountymobs";

    @Override
    public void onInitialize() {
        // Config
        ModConfig.init();

        // Bounty registry
        SpoornBountyRegistry.init();
    }
}
