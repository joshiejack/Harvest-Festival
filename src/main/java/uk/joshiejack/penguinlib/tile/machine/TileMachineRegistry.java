package uk.joshiejack.penguinlib.tile.machine;

import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import net.minecraft.item.ItemStack;

public abstract class TileMachineRegistry extends TileMachineSimple {
    private final HolderRegistry<ItemStack> registry;

    public TileMachineRegistry(HolderRegistry<ItemStack> registry, String time) {
        super(time);
        this.registry = registry;
    }

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return !registry.getValue(stack).isEmpty();
    }

    @Override
    public void finishMachine() {
        handler.setStackInSlot(0, registry.getValue(handler.getStackInSlot(0)).copy()); //Hell yeah!
    }
}

