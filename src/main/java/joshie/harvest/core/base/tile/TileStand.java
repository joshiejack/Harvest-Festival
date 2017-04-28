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
    @Nonnull
    protected ItemStack stack = ItemStack.EMPTY;
    private UUID uuid;

    public boolean isItemValid(@Nonnull ItemStack held) {
        return true;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void setContents(@Nonnull ItemStack stack) {
        this.stack = stack;
        saveAndRefresh();
    }

    public boolean setContents(@Nullable EntityPlayer player, ItemStack stack) {
        this.stack = stack.splitStack(1); //Remove one item
        this.uuid = EntityHelper.getPlayerUUID(player);
        saveAndRefresh();
        return true;
    }

    @Nonnull
    public ItemStack removeContents() {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        else {
            ItemStack stack = this.stack.copy();
            this.stack = ItemStack.EMPTY;
            saveAndRefresh();
            return stack;
        }
    }

    @Nonnull
    public ItemStack getContents() {
        return stack;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        stack = nbt.hasKey("Stack") ? NBTHelper.readItemStack(nbt.getCompoundTag("Stack")) : ItemStack.EMPTY;
        uuid = nbt.hasKey("UUID") ? UUID.fromString(nbt.getString("UUID")) : null;
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        if (!stack.isEmpty()) nbt.setTag("Stack", NBTHelper.writeItemStack(stack, new NBTTagCompound()));
        if (uuid != null) nbt.setString("UUID", uuid.toString());
        return super.writeToNBT(nbt);
    }
}
