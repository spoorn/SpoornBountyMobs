package org.spoorn.spoornbountymobs.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import org.spoorn.spoornbountymobs.SpoornBountyTiers;

/**
 * Implementation of SpoornEntityDataComponent per Entity.
 */
@ToString
public class SpoornBountyHostileEntityDataComponent implements SpoornEntityDataComponent, AutoSyncedComponent {

    private static final String HAS_BOUNTY = "hasBounty";
    private static final String SPOORN_BOUNTY_TIER = "spoornBountyTier";

    @Setter private boolean hasBounty;
    @Getter @Setter private SpoornBountyTiers spoornBountyTier;
    private Object provider;

    public SpoornBountyHostileEntityDataComponent(Object provider) {
        this.hasBounty = false;
        this.spoornBountyTier = SpoornBountyTiers.COMMON;
        this.provider = provider;
    }

    @Override
    public boolean hasBounty() {
        return this.hasBounty;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.hasBounty = tag.getBoolean(HAS_BOUNTY);
        this.spoornBountyTier = SpoornBountyTiers.fromValue(tag.getString(SPOORN_BOUNTY_TIER));
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean(HAS_BOUNTY, hasBounty);
        tag.putString(SPOORN_BOUNTY_TIER, this.spoornBountyTier.getValue());
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
