package uk.joshiejack.horticulture.tileentity;

import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.TileMachineRegistryDouble;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@PenguinLoader("seed_maker")
public class TileSeedMaker extends TileMachineRegistryDouble {
    public static final HolderRegistry<ItemStack> registry = new HolderRegistry<>(ItemStack.EMPTY);

    public TileSeedMaker() {
        super(registry, "half_hour");
    }

    @SideOnly(Side.CLIENT)
    public boolean hasSeeds() {
        ItemStack inSlot = getStack();
        return !inSlot.isEmpty() && registry.getValue(inSlot).isEmpty();
    }

    @Override
    public void finishMachine() {
        ItemStack result = registry.getValue(handler.getStackInSlot(0)).copy();
        int chance = world.rand.nextInt(100);
        if (chance > 90) result.setCount(3);
        else if (chance > 30) result.setCount(2);
        handler.setStackInSlot(0, result);
    }
}

