package joshie.harvest.core.tile;

import joshie.harvest.core.base.tile.TileSingleStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBasket extends TileSingleStack {
    public static final int BASKET_INVENTORY = 27;
    public static final Item STONE = Item.getItemFromBlock(Blocks.STONE);
    public final ItemStackHandler handler = new ItemStackHandler(BASKET_INVENTORY);

    @Override
    public boolean onRightClicked(EntityPlayer player, ItemStack place) {
        return false;
    }

    public boolean setAppearanceAndContents(@Nullable ItemStack place, ItemStackHandler handler) {
        if (place != null && place.getItem() != STONE) stack = place;
        for (int i = 0; i < handler.getSlots(); i++) {
            this.handler.setStackInSlot(i, handler.getStackInSlot(i));
        }

        saveAndRefresh();
        return false;
    }

    public boolean setAppearanceAndContents(@Nullable ItemStack place, NBTTagCompound inventory) {
        ItemStackHandler handler = new ItemStackHandler(BASKET_INVENTORY);
        handler.deserializeNBT(inventory); //Load from the nbt the inventory
        return setAppearanceAndContents(place, handler);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        handler.deserializeNBT(nbt.getCompoundTag("inventory"));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("inventory", handler.serializeNBT());
        return super.writeToNBT(nbt);
    }
}

