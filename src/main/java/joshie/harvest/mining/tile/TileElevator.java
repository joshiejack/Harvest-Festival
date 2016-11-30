package joshie.harvest.mining.tile;

import joshie.harvest.core.base.tile.TileFaceable;
import joshie.harvest.mining.MiningHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileElevator extends TileFaceable {
    private BlockPos twin;

    public BlockPos getTwin() {
        if (twin == null) {
            TileEntity tile = worldObj.getTileEntity(pos.down());
            if (tile instanceof TileElevator) {
                return ((TileElevator)tile).twin;
            }

            return null;
        } else return twin;
    }

    public void onBreakBlock() {
        TileElevator elevator = (TileElevator) worldObj.getTileEntity(twin);
        if (elevator != null) {
            elevator.twin = null;
            elevator.markDirty();
        }
    }

    public void setTwin(BlockPos twin) {
        TileElevator elevator = (TileElevator) worldObj.getTileEntity(twin);
        if (elevator != null) {
            elevator.twin = new BlockPos(pos);
            elevator.markDirty();

            //Update the sign above the linked block
            TileEntity tile = worldObj.getTileEntity(twin.up(2).offset(getFacing()));
            if (tile instanceof TileEntitySign) {
                TileEntitySign sign = ((TileEntitySign)tile);
                sign.signText[1] = new TextComponentTranslation("harvestfestival.elevator.to");
                sign.signText[2] = new TextComponentString("" + MiningHelper.getFloor(pos));
                sign.markDirty();
                IBlockState state = worldObj.getBlockState(sign.getPos());
                worldObj.notifyBlockUpdate(sign.getPos(), state, state, 3);
            }
        }

        this.twin = twin;
        this.markDirty();

        //Update the sign above this block
        TileEntity tile = worldObj.getTileEntity(pos.up(2).offset(getFacing()));
        if (tile instanceof TileEntitySign) {
            TileEntitySign sign = ((TileEntitySign)tile);
            sign.signText[1] = new TextComponentTranslation("harvestfestival.elevator.to");
            sign.signText[2] = new TextComponentString("" + MiningHelper.getFloor(twin));
            sign.markDirty();
            IBlockState state = worldObj.getBlockState(sign.getPos());
            worldObj.notifyBlockUpdate(sign.getPos(), state, state, 3);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Twin"))
            twin = BlockPos.fromLong(nbt.getLong("Twin"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (twin != null)
            nbt.setLong("Twin", twin.toLong());
        return super.writeToNBT(nbt);
    }
}
