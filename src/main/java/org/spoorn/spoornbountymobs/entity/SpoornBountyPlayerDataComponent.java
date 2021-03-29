package org.spoorn.spoornbountymobs.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.nbt.CompoundTag;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;

@ToString
public class SpoornBountyPlayerDataComponent implements PlayerDataComponent, AutoSyncedComponent {

    private static final String COMMON_COUNT = "commonKillCount";
    private static final String UNCOMMON_COUNT = "uncommonKillCount";
    private static final String RARE_COUNT = "rareKillCount";
    private static final String EPIC_COUNT = "epicKillCount";
    private static final String LEGENDARY_COUNT = "legendaryKillCount";
    private static final String DOOM_COUNT = "doomKillCount";

    private Object provider;
    @Getter
    private int commonKillCount;
    @Getter
    private int uncommonKillCount;
    @Getter
    private int rareKillCount;
    @Getter
    private int epicKillCount;
    @Getter
    private int legendaryKillCount;
    @Getter
    private int doomKillCount;

    public SpoornBountyPlayerDataComponent(Object provider) {
        this.provider = provider;
    }

    @Override
    public void incrementBountyKillCount(SpoornBountyTier spoornBountyTier) {
        switch (spoornBountyTier.getTierType()) {
            case COMMON_TIER:
                this.commonKillCount++;
                break;
            case UNCOMMON_TIER:
                this.uncommonKillCount++;
                break;
            case RARE_TIER:
                this.rareKillCount++;
                break;
            case EPIC_TIER:
                this.epicKillCount++;
                break;
            case LEGENDARY_TIER:
                this.legendaryKillCount++;
                break;
            case DOOM_TIER:
                this.doomKillCount++;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + spoornBountyTier.getTierType());
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.commonKillCount = tag.getInt(COMMON_COUNT);
        this.uncommonKillCount = tag.getInt(UNCOMMON_COUNT);
        this.rareKillCount = tag.getInt(RARE_COUNT);
        this.epicKillCount = tag.getInt(EPIC_COUNT);
        this.legendaryKillCount = tag.getInt(LEGENDARY_COUNT);
        this.doomKillCount = tag.getInt(DOOM_COUNT);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putInt(COMMON_COUNT, this.commonKillCount);
        tag.putInt(UNCOMMON_COUNT, this.uncommonKillCount);
        tag.putInt(RARE_COUNT, this.rareKillCount);
        tag.putInt(EPIC_COUNT, this.epicKillCount);
        tag.putInt(LEGENDARY_COUNT, this.legendaryKillCount);
        tag.putInt(DOOM_COUNT, this.doomKillCount);
    }
}
