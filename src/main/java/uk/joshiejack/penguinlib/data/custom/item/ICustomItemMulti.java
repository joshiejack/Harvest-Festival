package uk.joshiejack.penguinlib.data.custom.item;

import net.minecraft.item.ItemStack;

public interface ICustomItemMulti extends ICustomItem {
    ItemStack getStackFromString(String name, int count);
}
