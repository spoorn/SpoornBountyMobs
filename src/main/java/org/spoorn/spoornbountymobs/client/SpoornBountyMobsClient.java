package org.spoorn.spoornbountymobs.client;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Log4j2
public class SpoornBountyMobsClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        log.info("Hello Client from SpoornBountyMobs!");
    }
}
