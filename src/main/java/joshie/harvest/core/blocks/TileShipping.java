package joshie.harvest.core.blocks;

import joshie.harvest.core.base.TileFaceable;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import java.util.UUID;

import static joshie.harvest.api.HFApi.shipping;

public class TileShipping extends TileFaceable {
    private UUID owner;

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        this.markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (nbt.getString("Owner") != null) {
            owner = UUID.fromString(nbt.getString("Owner"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (owner != null) {
            nbt.setString("Owner", owner.toString());
        }

        return super.writeToNBT(nbt);
    }

    /** Capabilities **/
    private IItemHandler handler = new EmptyHandler() {
        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack == null || stack.getItem() == null || owner == null) return stack;
            long sell = shipping.getSellValue(stack);
            if (sell > 0) {
                if (!simulate && !getWorld().isRemote) {
                    HFTrackers.getPlayerTrackerFromUUID(owner).getTracking().addForShipping(stack.copy());
                }

                ItemStack copy = stack.copy();
                copy.splitStack(1);
                return copy;
            }

            return stack;
        }
    };

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, net.minecraft.util.EnumFacing facing) {
        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.util.EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) handler;
        return super.getCapability(capability, facing);
    }
}
