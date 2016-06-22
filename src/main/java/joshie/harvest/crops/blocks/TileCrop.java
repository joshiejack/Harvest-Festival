package joshie.harvest.crops.blocks;

import joshie.harvest.blocks.tiles.TileDaily;
import joshie.harvest.crops.CropData;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.blocks.BlockHFCrops.Stage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import static joshie.harvest.core.network.PacketHandler.sendRefreshPacket;
import static joshie.harvest.crops.HFCrops.NULL_CROP;

public class TileCrop extends TileDaily {
    public static class TileWithered extends TileCrop {
        @Override
        public void newDay(World world) {}

        @Override
        public void validate() {
            tileEntityInvalid = false;
        }

        @Override
        public void invalidate() {
            tileEntityInvalid = true;
        }
    }

    protected CropData data;
    protected boolean remove;

    //Return and create new data if it doesn't exist yet
    public CropData getData() {
        if (data == null) {
            data = new CropData(getPos());
            markDirty();
        }

        return data;
    }

    @Override
    public void newDay(World world) {
        //Rain check
        if (world.isRainingAt(getPos().up())) {
            getData().setHydrated(); //If today is raining, hydrate the crop automatically
        }

        remove = !getData().newDay(getWorld());
        if (!remove) {
            markDirty();
            sendRefreshPacket(this);
        }
    }

    @Override
    public boolean isInvalid() {
        return remove || super.isInvalid();
    }

    @Override
    public void onInvalidated() {
        if (data.getCrop() == null || data.getCrop() == NULL_CROP);
        else {
            if (data.getCrop().isDouble(data.getStage())) {
                getWorld().setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(BlockHFCrops.Stage.WITHERED_DOUBLE), 2);
            }

            //Prepare to save old data
            NBTTagCompound data = new NBTTagCompound();
            writeToNBT(data); //Save the old data

            getWorld().setBlockState(pos, HFCrops.CROPS.getStateFromEnum(Stage.WITHERED), 2);

            TileCrop crop = (TileCrop) getWorld().getTileEntity(pos);
            readFromNBT(data); //Copy over the data as we change the state
            crop.saveAndRefresh(); //And reload
        }
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        remove = nbt.getBoolean("Remove");
        data = new CropData(getPos());
        if (nbt.hasKey("CropData")) {
            data.readFromNBT(nbt.getCompoundTag("CropData"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("Remove", remove);
        if (data != null) {
            nbt.setTag("CropData", data.writeToNBT(new NBTTagCompound()));
        }

        return super.writeToNBT(nbt);
    }
}
