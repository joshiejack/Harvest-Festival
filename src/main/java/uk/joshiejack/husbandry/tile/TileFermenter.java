package uk.joshiejack.husbandry.tile;

import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.TileMachineRegistryDouble;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@PenguinLoader("fermenter")
public class TileFermenter extends TileMachineRegistryDouble {
    public static final HolderRegistry<ItemStack> registry = new HolderRegistry<>(ItemStack.EMPTY);

    public TileFermenter() {
        super(registry, "half_day");
    }
}
