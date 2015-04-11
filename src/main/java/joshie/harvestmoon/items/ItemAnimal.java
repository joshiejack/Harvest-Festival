package joshie.harvestmoon.items;

import net.minecraft.item.ItemStack;

public class ItemAnimal extends ItemHMMeta {
    public static final int CHICKEN = 0;
    public static final int SHEEP = 1;
    public static final int COW = 2;

    @Override
    public String getName(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case CHICKEN:
                return "chicken";
            case SHEEP:
                return "sheep";
            case COW:
                return "cow";
            default:
                return "null";
        }
    }

    @Override
    public int getMetaCount() {
        return 0;
    }
}
