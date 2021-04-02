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

    private static final String HAS_TRACKED = "hasTracked";
    private static final String HAS_BOUNTY = "hasBounty";
    private static final String SPOORN_BOUNTY_TIER = "spoornBountyTier";
    private static final String BONUS_BOUNTY_TIER_HEALTH = "bonusBountyTierHP";
    private static final String HAS_WEAKNESS_ATTACK = "hasWeaknessAtk";
    private static final String HAS_WITHER_ATTACK = "hasWitherAtk";
    private static final String HAS_BLINDNESS_ATTACK = "hasBlindnessAttack";
    private static final String HAS_POISON_ATTACK = "hasPoisonAtk";
    private static final String HAS_SLOWNESS_ATTACK = "hasSlowAtk";
    private static final String HAS_HUNGER_ATTACK = "hasHungerAtk";
    private static final String HAS_BURNING_ATTACK = "hasBurningAtk";

    private boolean hasTracked;
    @Setter private boolean hasBounty;
    @Getter @Setter private SpoornBountyTier spoornBountyTier;
    @Getter @Setter private float bonusBountyTierHealth;
    @Setter private boolean hasWeaknessAttack;
    @Setter private boolean hasWitherAttack;
    @Setter private boolean hasBlindnessAttack;
    @Setter private boolean hasPoisonAttack;
    @Setter private boolean hasSlownessAttack;
    @Setter private boolean hasHungerAttack;
    @Setter private boolean hasBurningAttack;
    private Object provider;

    public SpoornBountyHostileEntityDataComponent(Object provider) {
        this.hasTracked = false;
        this.hasBounty = false;
        this.spoornBountyTier = SpoornBountyTier.COMMON;
        this.provider = provider;
    }

    @Override
    public boolean hasTracked() {
        return this.hasTracked;
    }

    @Override
    public void track() {
        this.hasTracked = true;
    }

    @Override
    public boolean hasBounty() {
        return this.hasBounty;
    }

    @Override
    public boolean hasWeaknessAttack() {
        return this.hasWeaknessAttack;
    }

    @Override
    public boolean hasWitherAttack() {
        return this.hasWitherAttack;
    }

    @Override
    public boolean hasBlindnessAttack() {
        return this.hasBlindnessAttack;
    }

    @Override
    public boolean hasPoisonAttack() {
        return this.hasPoisonAttack;
    }

    @Override
    public boolean hasSlownessAttack() {
        return this.hasSlownessAttack;
    }

    @Override
    public boolean hasHungerAttack() {
        return this.hasHungerAttack;
    }

    @Override
    public boolean hasBurningAttack() {
        return this.hasBurningAttack;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.hasTracked = tag.getBoolean(HAS_TRACKED);
        this.hasBounty = tag.getBoolean(HAS_BOUNTY);
        this.spoornBountyTier = SpoornBountyTier.fromValue(SpoornBountyTierTypes.valueOf(tag.getString(SPOORN_BOUNTY_TIER)));
        this.bonusBountyTierHealth = tag.getFloat(BONUS_BOUNTY_TIER_HEALTH);
        this.hasWeaknessAttack = tag.getBoolean(HAS_WEAKNESS_ATTACK);
        this.hasWitherAttack = tag.getBoolean(HAS_WITHER_ATTACK);
        this.hasBlindnessAttack = tag.getBoolean(HAS_BLINDNESS_ATTACK);
        this.hasPoisonAttack = tag.getBoolean(HAS_POISON_ATTACK);
        this.hasSlownessAttack = tag.getBoolean(HAS_SLOWNESS_ATTACK);
        this.hasHungerAttack = tag.getBoolean(HAS_HUNGER_ATTACK);
        this.hasBurningAttack = tag.getBoolean(HAS_BURNING_ATTACK);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean(HAS_TRACKED, this.hasTracked);
        tag.putBoolean(HAS_BOUNTY, this.hasBounty);
        // Could use ordinal for efficiency in future, but could cause compatibility complexities
        tag.putString(SPOORN_BOUNTY_TIER, this.spoornBountyTier.getTierType().name());
        tag.putFloat(BONUS_BOUNTY_TIER_HEALTH, this.bonusBountyTierHealth);
        tag.putBoolean(HAS_WEAKNESS_ATTACK, this.hasWeaknessAttack);
        tag.putBoolean(HAS_WITHER_ATTACK, this.hasWitherAttack);
        tag.putBoolean(HAS_BLINDNESS_ATTACK, this.hasBlindnessAttack);
        tag.putBoolean(HAS_POISON_ATTACK, this.hasPoisonAttack);
        tag.putBoolean(HAS_SLOWNESS_ATTACK, this.hasSlownessAttack);
        tag.putBoolean(HAS_HUNGER_ATTACK, this.hasHungerAttack);
        tag.putBoolean(HAS_BURNING_ATTACK, this.hasBurningAttack);
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
