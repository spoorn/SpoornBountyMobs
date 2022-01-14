package org.spoorn.spoornbountymobs.entity.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.Identifier;
import org.spoorn.spoornbountymobs.SpoornBountyMobs;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;

/**
 * Base player data.
 */
public interface PlayerDataComponent extends ComponentV3 {

    Identifier ID = new Identifier(SpoornBountyMobs.MODID, "playerdata");

    int getCommonKillCount();

    int getUncommonKillCount();

    int getRareKillCount();

    int getEpicKillCount();

    int getLegendaryKillCount();

    int getDoomKillCount();

    void incrementBountyKillCount(SpoornBountyTier spoornBountyTier);

    double getBountyScore();
    void setBountyScore(double bountyScore);

    int getHighestBountyHunterLevel();
    void setHighestBountyHunterLevel(int tier);
}
