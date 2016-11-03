package joshie.harvest.core.base.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileSingleStack extends TileDaily {
    protected ItemStack stack = null;

    public abstract boolean onRightClicked(EntityPlayer player, ItemStack place);

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

        return super.writeToNBT(nbt);
    }
}
