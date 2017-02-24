package joshie.harvest.core.base.tile;

import joshie.harvest.core.util.interfaces.IFaceable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public class TileFaceable extends TileHarvest implements IFaceable {
    protected EnumFacing orientation;

    @Override
    public void setFacing(EnumFacing dir) {
        if (dir == EnumFacing.DOWN || dir == EnumFacing.UP) {
            orientation = EnumFacing.NORTH;
        } else orientation = dir;

        saveAndRefresh();
    }

    @Override
    public EnumFacing getFacing() {
        return orientation != null ? orientation : EnumFacing.NORTH;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        orientation = EnumFacing.byName(nbt.getString("Orientation"));
        if (orientation == null || orientation == EnumFacing.DOWN || orientation == EnumFacing.UP) {
            orientation = EnumFacing.NORTH;
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (orientation != null) {
            nbt.setString("Orientation", orientation.getName2());
        }

        return super.writeToNBT(nbt);
    }
}
