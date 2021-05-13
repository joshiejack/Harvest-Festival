package uk.joshiejack.husbandry.tile;

import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.TileMachineRegistry;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@PenguinLoader("spinning_wheel")
public class TileSpinningWheel extends TileMachineRegistry {
    public static final HolderRegistry<ItemStack> registry = new HolderRegistry<>(ItemStack.EMPTY);

    public TileSpinningWheel() {
        super(registry, "half_day");
    }
}
