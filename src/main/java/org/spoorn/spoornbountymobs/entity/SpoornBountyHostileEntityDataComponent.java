package org.spoorn.spoornbountymobs.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTierTypes;

/**
 * Implementation of SpoornEntityDataComponent per Entity.
 */
@ToString
public class SpoornBountyHostileEntityDataComponent implements EntityDataComponent, AutoSyncedComponent {

    private static final String HAS_BOUNTY = "hasBounty";
    private static final String SPOORN_BOUNTY_TIER = "spoornBountyTier";
    private static final String BONUS_HEALTH = "bonusHealth";

    @Setter private boolean hasBounty;
    @Getter @Setter private SpoornBountyTier spoornBountyTier;
    @Getter @Setter private float bonusHealth;
    private Object provider;

    public SpoornBountyHostileEntityDataComponent(Object provider) {
        this.hasBounty = false;
        this.spoornBountyTier = SpoornBountyTier.COMMON;
        this.provider = provider;
    }

    @Override
    public boolean hasBounty() {
        return this.hasBounty;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.hasBounty = tag.getBoolean(HAS_BOUNTY);
        this.spoornBountyTier = SpoornBountyTier.fromValue(SpoornBountyTierTypes.valueOf(tag.getString(SPOORN_BOUNTY_TIER)));
        this.bonusHealth = tag.getFloat(BONUS_HEALTH);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean(HAS_BOUNTY, this.hasBounty);
        // Could use ordinal for efficiency in future, but could cause compatibility complexities
        tag.putString(SPOORN_BOUNTY_TIER, this.spoornBountyTier.getTierType().name());
        tag.putFloat(BONUS_HEALTH, this.bonusHealth);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        // Call super
        AutoSyncedComponent.super.applySyncPacket(buf);

        // Recalculate entity dimensions how that we've marked it as having a bounty on client side
        if (this.hasBounty && this.provider instanceof Entity) {
            ((Entity) this.provider).calculateDimensions();
        }
    }
}
