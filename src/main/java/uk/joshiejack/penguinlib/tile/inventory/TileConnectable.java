package uk.joshiejack.penguinlib.tile.inventory;

import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSyncMembers;
import uk.joshiejack.penguinlib.network.packet.PacketSyncOffsets;
import uk.joshiejack.penguinlib.network.packet.PacketSurroundingsChanged;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileConnectable<T extends TileConnectable> extends TileSingleStack {
    private final int maxMembers;
    protected int members = 0;
    protected boolean facingEW; //If the master is "traveling along the x axis"
    protected int offsetX; //The offset from the master block
    protected int offsetZ; //The offset from the master block

    public TileConnectable(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    @SuppressWarnings("unchecked")
    public T getMasterBlock() {
       // if (members > 0 || (offsetX == 0 && offsetZ == 0)) return (T) this;
        BlockPos pos = this.pos.add(offsetX, 0, offsetZ);
        TileEntity tile = world.getTileEntity(pos);
        return isSameTile(pos) ? (T) tile: (T) this;
    }

    public ItemStack getStack() {
        return handler.getStackInSlot(0);
    }

    protected abstract boolean isSameTile(BlockPos pos);

    private boolean setMaster(TileConnectable tile, EnumFacing facing) {
        boolean EW = facing == EnumFacing.WEST || facing == EnumFacing.EAST;
        if (tile.members < maxMembers && (tile.members == 0 || tile.facingEW == EW)) {
            //We found a match so let's update the master numbers
            tile.members++; //Increase by one
            tile.facingEW = EW; //Update the master's facing direction so they know!
            tile.markDirty();
            if (!world.isRemote) {
                PenguinNetwork.sendToNearby(tile, new PacketSyncMembers(tile.getPos(), tile.members));
            }

            this.offsetX = tile.pos.getX() - this.pos.getX();//facing.getFrontOffsetX();
            this.offsetZ = tile.pos.getZ() - this.pos.getZ();//facing.getFrontOffsetZ();
            if (!world.isRemote) {
                PenguinNetwork.sendToNearby(this, new PacketSyncOffsets(pos, this.offsetX, this.offsetZ));
            }

            this.markDirty();
            return true;
        }

        return false;
    }

    @SuppressWarnings("ConstantConditions")
    public void onBlockPlaced() {
        //Look for a block next to us, we need to check if that block is a master
        //If that block IS a master AND the master hasn't reached maximum members AND (members == 0 OR (facing == same way)
        //Then we have permission to ADD this block as counting this one as it's master
        //Which should update this block offsetX and offsetZ, and increase the length of the master
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            BlockPos pos = this.pos.offset(facing);
            if (isSameTile(pos)) {
                if (setMaster(((TileConnectable) world.getTileEntity(pos)).getMasterBlock(), facing)) {//The block wasn't a master but a slave SO grab that slaves master
                    break;
                }
            }
        }

        onSurroundingsChanged();
    }

    public int getMembers() {
        return members;
    }

    @SideOnly(Side.CLIENT)
    public void setMembers(int members) {
        this.members = members;
    }

    @SideOnly(Side.CLIENT)
    public void setOffsets(int offsetX, int offsetZ) {
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
    }

    @Override
    public void dropInventory() {
        if (this == getMasterBlock()) {
            super.dropInventory();
        }
    }

    @SuppressWarnings("unchecked, ConstantConditions")
    @Nullable
    private T getNewMasterFrom(T oldMaster) {
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            if (isSameTile(oldMaster.pos.offset(facing))) {
                T tile = (T) world.getTileEntity(oldMaster.pos.offset(facing));
                if (tile.getMasterBlock() == oldMaster) {
                    return tile;
                }
            }
        }
        return null;
    }

    private static final EnumFacing[] EW = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST };
    private static final EnumFacing[] SN = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.NORTH };

    //This be the hard part?
    public void onBlockRemoved() {
        if (world.isRemote) return;
        //When a block is broken we need to do many things....
        //If this block wasn't a master block then we need to notify the master block itself
        //The master block needs to know that its surrounding have changed
        //First thing we'll check is if the master block has any blocks to the side of it, that have it as a master block
        //If this is not the case, then we need to search in the maximum radius on all sides for all blocks that are using this masterblock
        //With this information we should reset all of these tiles, so that they themselves are master blocks
        //We know that there will be one less member, so we need to split off from the master blocks inventory
        //The new size vs the old size of inventory
        T oldMaster = getMasterBlock();
        T newMaster = oldMaster == this ? getNewMasterFrom(oldMaster): oldMaster;

        ItemStack contents = oldMaster.getStack().copy();
        if (oldMaster != newMaster && newMaster != null) {
            newMaster.members = oldMaster.members;
            newMaster.facingEW = oldMaster.facingEW;
            newMaster.offsetX = 0;
            newMaster.offsetZ = 0;
            //We need to copy over all the data to the new master block
        } else newMaster = oldMaster;

        //We have one less member so let's make it so
        newMaster.members = MathsHelper.constrainToRangeInt(newMaster.members - 1, 0, maxMembers);
        //Now that we know how many members we have we can work out the stack leftover
        newMaster.handler.setStackInSlot(0, ItemStack.EMPTY);
        ItemStack leftovers = newMaster.handler.insertItem(0, contents, false);
        if (!leftovers.isEmpty()) {
            Block.spawnAsEntity(world, pos, leftovers);
        }

        //Everything is known, now we want to go and update all the slaves
        EnumFacing[] cardinals = newMaster.facingEW ? EW : SN;
        for (EnumFacing facing: cardinals) {
            for (int offset = 1; offset <= maxMembers; offset++) {
                updateMaster(newMaster.pos.offset(facing, offset), oldMaster, newMaster);
            }
        }

        newMaster.markDirty();
        PenguinNetwork.sendToNearby(newMaster, new PacketSyncMembers(newMaster.pos, newMaster.members));
        PenguinNetwork.sendToNearby(newMaster, new PacketSyncOffsets(newMaster.pos, newMaster.offsetX, newMaster.offsetZ));
        PenguinNetwork.sendToNearby(newMaster, new PacketSurroundingsChanged(newMaster.pos));
    }

    @SuppressWarnings("ConstantConditions, unchecked")
    private void updateMaster(BlockPos pos, T oldMaster, T newMaster) {
        if (isSameTile(pos)) {
            T multi = (T) world.getTileEntity(pos);
            if (multi.getMasterBlock() == oldMaster) {
                multi.offsetX = newMaster.pos.getX() - pos.getX();
                multi.offsetZ = newMaster.pos.getZ() - pos.getZ();
                multi.markDirty();
                PenguinNetwork.sendToNearby(multi, new PacketSyncOffsets(multi.pos, multi.offsetX, multi.offsetZ));
                PenguinNetwork.sendToNearby(multi, new PacketSurroundingsChanged(multi.pos));
            }
        }
    }

    public void onSurroundingsChanged() {
        if (!world.isRemote) {
            T master = getMasterBlock();
            PenguinNetwork.sendToNearby(master, new PacketSurroundingsChanged(master.pos));
        }
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        return getMasterBlock().onMasterClicked(player, hand);
    }

    protected abstract boolean onMasterClicked(EntityPlayer player, EnumHand hand);

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        facingEW = nbt.getBoolean("FacingEW");
        offsetX = nbt.getInteger("OffsetX");
        offsetZ = nbt.getInteger("OffsetZ");
        members = nbt.getByte("Members");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("FacingEW", facingEW);
        nbt.setInteger("OffsetX", offsetX);
        nbt.setInteger("OffsetZ", offsetZ);
        nbt.setByte("Members", (byte) members);
        return super.writeToNBT(nbt);
    }
}
