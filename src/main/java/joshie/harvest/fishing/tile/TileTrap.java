package joshie.harvest.fishing.tile;

import joshie.harvest.core.base.tile.TileDaily;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileTrap extends TileDaily {
    private ItemStack stack = null;

    @Override
    public void newDay() {
        if (stack != null && stack.getItem() == HFFishing.JUNK && stack.getItemDamage() == Junk.BAIT.ordinal()) {

        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Stack")) {
            stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Stack"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (stack != null) {
            nbt.setTag("Stack", stack.writeToNBT(new NBTTagCompound()));
        }

        return nbt;
    }
}
