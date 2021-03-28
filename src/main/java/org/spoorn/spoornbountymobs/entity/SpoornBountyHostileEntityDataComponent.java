package org.spoorn.spoornbountymobs.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spoorn.spoornbountymobs.SpoornBountyMobs;

/**
 * Implementation of SpoornEntityDataComponent per Entity.
 */
public class SpoornBountyHostileEntityDataComponent implements SpoornEntityDataComponent, AutoSyncedComponent {

    public static final Identifier ID = new Identifier(SpoornBountyMobs.MODID, "hostileentitydata");

    private static final String HAS_BOUNTY = "hasBounty";

    @Setter
    private boolean hasBounty;
    private Object provider;

    public SpoornBountyHostileEntityDataComponent(Object provider) {
        this.hasBounty = false;
        this.provider = provider;
    }

    @Override
    public boolean hasBounty() {
        return this.hasBounty;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.hasBounty = tag.getBoolean(HAS_BOUNTY);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean(HAS_BOUNTY, hasBounty);
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
