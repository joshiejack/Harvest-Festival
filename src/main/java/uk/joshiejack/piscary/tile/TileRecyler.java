package uk.joshiejack.piscary.tile;

import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.TileMachineRegistry;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;

@PenguinLoader("recycler")
public class TileRecyler extends TileMachineRegistry {
    public static final HolderRegistry<ItemStack> registry = new HolderRegistry<>(ItemStack.EMPTY);

    public TileRecyler() {
        super(registry, "hour");
    }
}
