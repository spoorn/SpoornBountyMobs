package org.spoorn.spoornbountymobs.mixin.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spoorn.spoornbountymobs.entity.component.EntityDataComponent;
import org.spoorn.spoornbountymobs.util.SpoornBountyMobsUtil;

@Pseudo
@Mixin(targets = "com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes", remap = false)
public class ReachEntityAttributesMixin {

    /**
     * Compatibility with JamiesWhiteShirt/reach-entity-attributes check on the reach range when allowing a player to
     * attach an entity:
     * 
     * https://github.com/JamiesWhiteShirt/reach-entity-attributes/blob/1.18.2/src/main/java/com/jamieswhiteshirt/reachentityattributes/ReachEntityAttributes.java#L66-L68
     * 
     * ReachEntityAttributes is a library used by mods such as Bewitchment.
     * 
     * Note: This is an @Pseudo mixin, so it does not check that this mixin actually applied.  If REA changes the code,
     * this mixin will need to be updated.
     */
    @Redirect(method = "isWithinAttackRange", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;squaredDistanceTo(Lnet/minecraft/entity/Entity;)D"), remap = false)
    private static double scaleMaxDistanceForInteractingWithEntityREACompat(PlayerEntity instance, Entity entity) {
        double original = instance.squaredDistanceTo(entity);
        if (SpoornBountyMobsUtil.entityIsHostileAndHasBounty(entity)) {
            EntityDataComponent entityDataComponent = SpoornBountyMobsUtil.getSpoornEntityDataComponent(entity);
            float scale = entityDataComponent.getSpoornBountyTier().getMobSizeScale();
            if (scale > 1) {
                return original / scale / scale;
            }
        }

        return original;
    }
}
