package org.spoorn.spoornbountymobs.entity;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.Identifier;
import org.spoorn.spoornbountymobs.SpoornBountyMobs;

public interface PlayerDataComponent extends ComponentV3 {

    Identifier ID = new Identifier(SpoornBountyMobs.MODID, "playerdata");

    int getCommonKillCount();
    void incrementCommonKillCount();

    int getUncommonKillCount();
    void incrementUncommonKillCount();

    int getRareKillCount();
    void incrementRareKillCount();

    int getEpicKillCount();
    void incrementEpicKillCount();

    int getLegendaryKillCount();
    void incrementLegendaryKillCount();

    int getDoomKillCount();
    void incrementDoomKillCount();
}
