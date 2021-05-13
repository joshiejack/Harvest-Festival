package uk.joshiejack.gastronomy.tile;

import uk.joshiejack.penguinlib.block.interfaces.IInteractable;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetFacing;
import uk.joshiejack.penguinlib.tile.TileWaterTank;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@PenguinLoader("sink")
public class TileSink extends TileWaterTank implements Rotatable, IInteractable {
    private EnumFacing facing = EnumFacing.NORTH;

    @Override
    protected FluidTank createTank() {
        FluidTank tank = new FluidTank(FluidRegistry.WATER, Fluid.BUCKET_VOLUME, Fluid.BUCKET_VOLUME) {
            @Nullable
            @Override
            public FluidStack drainInternal(int maxDrain, boolean doDrain) {
                if (fluid == null || maxDrain <= 0) {
                    return null;
                }

                int drained = maxDrain;
                if (fluid.amount < drained) {
                    drained = fluid.amount;
                }

                FluidStack stack = new FluidStack(fluid, drained);
                if (doDrain) {
                    onContentsChanged();
                    if (tile != null) {
                        FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid, tile.getWorld(), tile.getPos(), this, drained));
                    }
                }
                return stack;
            }
        };

        tank.setTileEntity(this);
        return tank;
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        FluidUtil.interactWithFluidHandler(player, hand, tank);
        return true;
    }

    @Override
    public void setFacing(EnumFacing facing) {
        this.facing = facing;
        this.markDirty();
        if (!world.isRemote) {
            PenguinNetwork.sendToNearby(this, new PacketSetFacing(pos, facing));
        }
    }

    @Nonnull
    @Override
    public EnumFacing getFacing() {
        return facing;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        facing = EnumFacing.values()[nbt.getByte("Facing")];
        if (facing == null || facing.getHorizontalIndex() < 0) facing = EnumFacing.NORTH;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("Facing", (byte) facing.ordinal());
        return super.writeToNBT(nbt);
    }
}
