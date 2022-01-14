package org.spoorn.spoornbountymobs.tiers;

import lombok.Getter;
import net.minecraft.util.Formatting;

/**
 * Tier enums.
 */
public enum SpoornBountyTierTypes {
    COMMON_TIER("COMMON", Formatting.GRAY),
    UNCOMMON_TIER("UNCOMMON", Formatting.GREEN),
    RARE_TIER("RARE", Formatting.BLUE),
    EPIC_TIER("EPIC", Formatting.DARK_PURPLE),
    LEGENDARY_TIER("LEGENDARY", Formatting.GOLD),
    DOOM_TIER("DOOM", Formatting.DARK_RED);

    @Getter private final String name;
    @Getter private final Formatting[] formattings;

    SpoornBountyTierTypes(String name, Formatting... formatting) {
        this.name = name;
        formattings = formatting;
    }
}
