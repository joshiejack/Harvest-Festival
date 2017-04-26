package joshie.harvest.core.base.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public abstract class TileSingleStack extends TileHarvest {
    @Nonnull
    protected ItemStack stack = ItemStack.EMPTY;

    public abstract boolean onRightClicked(EntityPlayer player, @Nonnull ItemStack place);

    @Nonnull
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        stack = new ItemStack(nbt.getCompoundTag("Stack"));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Stack", stack.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(nbt);
    }
}
