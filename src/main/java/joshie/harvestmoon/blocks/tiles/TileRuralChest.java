package joshie.harvestmoon.blocks.tiles;

import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSyncOrientation;
import joshie.harvestmoon.util.generic.IFaceable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class TileRuralChest extends TileEntity implements IFaceable {
    private ForgeDirection orientation = ForgeDirection.NORTH;
    private boolean isTop;

    @Override
    public void setFacing(ForgeDirection dir) {
        orientation = dir;
    }

    @Override
    public ForgeDirection getFacing() {
        return orientation;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    public IMessage getPacket() {
        return new PacketSyncOrientation(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, orientation);
    }

    @Override
    public Packet getDescriptionPacket() {
        return PacketHandler.getPacket(getPacket());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        orientation = ForgeDirection.getOrientation(nbt.getInteger("Orientation"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("Orientation", orientation.ordinal());
    }
}
