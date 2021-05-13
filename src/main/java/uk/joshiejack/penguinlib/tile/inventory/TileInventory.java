package uk.joshiejack.penguinlib.tile.inventory;

import uk.joshiejack.penguinlib.PenguinConfig;
import uk.joshiejack.penguinlib.block.interfaces.IInteractable;
import uk.joshiejack.penguinlib.tile.TilePenguin;
import uk.joshiejack.penguinlib.util.handlers.ValidatedStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileInventory extends TilePenguin implements IInteractable {
    protected ValidatedStackHandler handler;

    public TileInventory(int size) {
        handler = createHandler(size);
    }

    public abstract boolean isStackValidForInsertion(int slot, ItemStack stack);
    public abstract boolean isSlotValidForExtraction(int slot, int amount);

    /** Only called if applicable **/
    public void updateTick() {}

    protected ValidatedStackHandler createHandler(int size) {
        return new ValidatedStackHandler(this, size);
    }

    public ValidatedStackHandler getHandler() {
        return handler;
    }

    public void dropInventory() {
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack itemstack = handler.getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                InventoryHelper.spawnItemStack(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemstack);
            }
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return (!PenguinConfig.enableAutomation && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) handler;
        return super.getCapability(capability, facing);
    }

    public abstract boolean onRightClicked(EntityPlayer player, EnumHand hand);

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        handler.deserializeNBT(nbt.getCompoundTag("Inventory"));
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Inventory", handler.serializeNBT());
        return super.writeToNBT(nbt);
    }

    public void onContentsChanged(int slot){}
}
