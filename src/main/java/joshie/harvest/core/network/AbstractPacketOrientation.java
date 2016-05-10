package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class AbstractPacketOrientation extends AbstractPacketLocation {
    private EnumFacing dir;

    public AbstractPacketOrientation() {}

    public AbstractPacketOrientation(int dim, BlockPos pos, EnumFacing dir) {
        super(dim, pos);
        this.dir = dir == null ? EnumFacing.NORTH : dir;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeInt(dir.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        dir = EnumFacing.values()[(buffer.readInt())];
    }

    public void handlePacket(EntityPlayer player) {
        TileEntity tile = MCClientHelper.getTile(this);
        if (tile instanceof IFaceable) {
            ((IFaceable) player.worldObj.getTileEntity(pos)).setFacing(dir);
            MCClientHelper.updateRender(pos);
        }
    }
}