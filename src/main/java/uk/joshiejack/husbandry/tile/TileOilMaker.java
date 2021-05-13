package uk.joshiejack.husbandry.tile;

import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.TileMachineRegistry;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@PenguinLoader("oil_maker")
public class TileOilMaker extends TileMachineRegistry {
    public static final HolderRegistry<ItemStack> registry = new HolderRegistry<>(ItemStack.EMPTY);

    public TileOilMaker() {
        super(registry, "day");
    }
}
