package uk.joshiejack.economy.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ItemGetValueEvent extends Event {
    private final ItemStack stack;
    private final long value;
    private long newValue;

    public ItemGetValueEvent(ItemStack stack, long value) {
        this.stack = stack;
        this.value = value;
        this.newValue = value;
    }

    public ItemStack getStack() {
        return stack;
    }

    public long getValue() {
        return value;
    }

    public void setNewValue(long value) {
        this.newValue = value;
    }

    public long getNewValue() {
        return newValue;
    }
}
