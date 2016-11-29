package joshie.harvest.mining.tile;

import joshie.harvest.core.base.tile.TileHarvest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class TileElevator extends TileHarvest {
    private BlockPos twin;

    public void setTwin(BlockPos twin) {
        this.twin = twin;
        this.markDirty();
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
