package joshie.harvest.core.tile;

import joshie.harvest.core.base.tile.TileFaceable;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileStand extends TileFaceable {
    protected ItemStack stack;

    public boolean isItemValid(@Nonnull ItemStack held) {
        return true;
    }

    public boolean isEmpty() {
        return stack == null;
    }

    public void setContents(ItemStack stack) {
        this.stack = stack;
        this.markDirty();
    }

    public boolean setContents(@Nullable EntityPlayer player, ItemStack stack) {
        this.stack = stack.splitStack(1); //Remove one item
        MCServerHelper.markTileForUpdate(this);
        return true;
    }

    public ItemStack removeContents() {
        if (stack == null) return null;
        else {
            ItemStack stack = this.stack.copy();
            this.stack = null;
            MCServerHelper.markTileForUpdate(this);
            return stack;
        }
    }

    public ItemStack getContents() {
        return stack;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        stack = nbt.hasKey("Stack") ? NBTHelper.readItemStack(nbt.getCompoundTag("Stack")) : null;
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (stack != null) nbt.setTag("Stack", NBTHelper.writeItemStack(stack, new NBTTagCompound()));
        return super.writeToNBT(nbt);
    }
}
