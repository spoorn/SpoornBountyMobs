package org.spoorn.spoornbountymobs.util;

import lombok.AllArgsConstructor;
import lombok.ToString;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;

import javax.annotation.Nullable;
import java.util.List;

@AllArgsConstructor
@ToString
public class ItemInfo {

    public int count;
    public List<Item> items;
    @Nullable
    public NbtCompound nbt;
}
