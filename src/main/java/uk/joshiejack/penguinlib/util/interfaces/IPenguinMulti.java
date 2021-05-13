package uk.joshiejack.penguinlib.util.interfaces;

import net.minecraft.item.ItemStack;

public interface IPenguinMulti<E extends Enum> {
    E[] getValues();
    ItemStack getStackFromEnumString(String name, int count);
}
