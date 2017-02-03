package joshie.harvest.core.tile;

import joshie.harvest.core.base.tile.TileFaceable;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import javax.annotation.Nonnull;
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

        if (nbt.hasKey("Owner")) owner = UUID.fromString(nbt.getString("Owner"));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (owner != null) nbt.setString("Owner", owner.toString());
        return super.writeToNBT(nbt);
    }

    /** Capabilities **/
    private final IItemHandler handler = new EmptyHandler() {
        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack == null || stack.getItem() == null || owner == null) return stack;
            long sell = shipping.getSellValue(stack);
            if (sell > 0) {
                if (!simulate && !getWorld().isRemote) {
                    PlayerTrackerServer tracker = HFTrackers.getPlayerTracker(getWorld(), owner);
                    if (tracker != null) {
                        tracker.getTracking().addForShipping(StackHelper.toStack(stack, 1));
                    } else return stack;
                }

                ItemStack copy = stack.copy();
                copy.splitStack(1);
                return copy;
            }

            return stack;
        }
    };

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) handler;
        return super.getCapability(capability, facing);
    }
}
