package uk.joshiejack.penguinlib.client.gui;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CyclingStack {
    private final NonNullList<ItemStack> stacks;
    private ItemStack current = ItemStack.EMPTY;
    private long ticker = 0;
    protected int id;

    public CyclingStack(NonNullList<ItemStack> stacks) {
        this.stacks = stacks;
    }

    private ItemStack getStackFromID(int withOffset) {
        int id = withOffset % stacks.size();
        if (id < 0) id = stacks.size();
        if (id >= stacks.size()) {
            id = 0;
        }

        return stacks.get(id);
    }

    public ItemStack getStack(int offset) {
        if (stacks.isEmpty()) return new ItemStack(Items.CLAY_BALL);
        if (System.currentTimeMillis() - ticker > 2500) {
            ticker = System.currentTimeMillis();
            id++;

            if (id >= stacks.size()) {
                id = 0; //Reset to 0
            }
        }

        return getStackFromID(id + offset);
    }
}
