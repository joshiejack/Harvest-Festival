package uk.joshiejack.harvestcore.tile;

import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.TileMachineRegistryDouble;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;

@PenguinLoader("kiln")
public class TileKiln extends TileMachineRegistryDouble {
    public static final HolderRegistry<ItemStack> registry = new HolderRegistry<>(ItemStack.EMPTY);

    public TileKiln() {
        super(registry, "half_hour");
    }
}

