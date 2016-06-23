package joshie.harvest.blocks.tiles;

import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public abstract class TileFillableSizedFaceable extends TileFillableSized implements IFaceable {
    public EnumFacing orientation;

    @Override
    public void setFacing(EnumFacing dir) {
        if (dir == EnumFacing.DOWN || dir == EnumFacing.UP) {
            orientation = EnumFacing.NORTH;
        } else orientation = dir;
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
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (orientation != null) {
            nbt.setString("Orientation", orientation.getName2());
        }

        return super.writeToNBT(nbt);
    }
}
