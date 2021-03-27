package org.spoorn.spoornbountymobs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessorMixin {

    @Accessor("dimensions")
    public EntityDimensions getEntityDimensions();
}
