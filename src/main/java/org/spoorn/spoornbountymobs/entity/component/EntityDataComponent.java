package org.spoorn.spoornbountymobs.entity.component;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.Identifier;
import org.spoorn.spoornbountymobs.SpoornBountyMobs;
import org.spoorn.spoornbountymobs.tiers.SpoornBountyTier;

/**
 * Base SpoornEntityData.
 */
public interface EntityDataComponent extends ComponentV3 {

    Identifier ID = new Identifier(SpoornBountyMobs.MODID, "hostileentitydata");

    boolean hasTracked();
    void track();

    boolean hasBounty();
    void setHasBounty(boolean hasBounty);

    SpoornBountyTier getSpoornBountyTier();
    void setSpoornBountyTier(SpoornBountyTier spoornBountyTier);

    float getBonusBountyLevelHealth();
    void setBonusBountyLevelHealth(float bonusHealth);

    boolean hasWeaknessAttack();
    void setHasWeaknessAttack(boolean hasWeaknessSkill);

    boolean hasWitherAttack();
    void setHasWitherAttack(boolean hasWitherSkill);

    boolean hasBlindnessAttack();
    void setHasBlindnessAttack(boolean hasBlindnessAttack);

    boolean hasPoisonAttack();
    void setHasPoisonAttack(boolean hasPoisonAttack);

    boolean hasSlownessAttack();
    void setHasSlownessAttack(boolean hasSlownessAttack);

    boolean hasHungerAttack();
    void setHasHungerAttack(boolean hasHungerAttack);

    boolean hasBurningAttack();
    void setHasBurningAttack(boolean hasBurningAttack);
}
