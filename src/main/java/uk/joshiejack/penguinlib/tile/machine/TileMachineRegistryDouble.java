package uk.joshiejack.penguinlib.tile.machine;

import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import net.minecraft.item.ItemStack;

public abstract class TileMachineRegistryDouble extends TileMachineRegistry implements DoubleMachine {
    public TileMachineRegistryDouble(HolderRegistry<ItemStack> registry, String time) {
        super(registry, time);
    }
}

