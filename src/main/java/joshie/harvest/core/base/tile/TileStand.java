package joshie.harvest.core.base.tile;

import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TileStand extends TileFaceable {
    protected ItemStack stack;
    private UUID uuid;

    public boolean isItemValid(@Nonnull ItemStack held) {
        return true;
    }

    public boolean isEmpty() {
        return stack == null;
    }

    public void setContents(ItemStack stack) {
        this.stack = stack;
        saveAndRefresh();
    }

    public boolean setContents(@Nullable EntityPlayer player, ItemStack stack) {
        this.stack = stack.splitStack(1); //Remove one item
        this.uuid = EntityHelper.getPlayerUUID(player);
        saveAndRefresh();
        return true;
    }

    public ItemStack removeContents() {
        if (stack == null) return null;
        else {
            ItemStack stack = this.stack.copy();
            this.stack = null;
            saveAndRefresh();
            return stack;
        }
    }

    public ItemStack getContents() {
        return stack;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        stack = nbt.hasKey("Stack") ? NBTHelper.readItemStack(nbt.getCompoundTag("Stack")) : null;
        uuid = nbt.hasKey("UUID") ? UUID.fromString(nbt.getString("UUID")) : null;
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        if (stack != null) nbt.setTag("Stack", NBTHelper.writeItemStack(stack, new NBTTagCompound()));
        if (uuid != null) nbt.setString("UUID", uuid.toString());
        return super.writeToNBT(nbt);
    }
}
