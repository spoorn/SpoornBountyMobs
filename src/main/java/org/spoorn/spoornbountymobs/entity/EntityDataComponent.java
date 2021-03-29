package org.spoorn.spoornbountymobs.entity;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.Identifier;
import org.spoorn.spoornbountymobs.SpoornBountyMobs;
import org.spoorn.spoornbountymobs.SpoornBountyTier;

/**
 * Base SpoornEntityData.
 */
public interface EntityDataComponent extends ComponentV3 {

    Identifier ID = new Identifier(SpoornBountyMobs.MODID, "hostileentitydata");

    boolean hasBounty();

    void setHasBounty(boolean hasBounty);

    SpoornBountyTier getSpoornBountyTier();

    void setSpoornBountyTier(SpoornBountyTier spoornBountyTier);
}
