package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class AbstractPacketOrientation extends AbstractPacketLocation {
    private EnumFacing dir;

    public AbstractPacketOrientation() {
    }

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

    public IMessage onMessage(AbstractPacketOrientation message, MessageContext ctx) {
        EntityPlayer player = MCClientHelper.getPlayer();
        TileEntity tile = MCClientHelper.getTile(message);
        if (tile instanceof IFaceable) {
            ((IFaceable) player.worldObj.getTileEntity(message.pos)).setFacing(message.dir);
            MCClientHelper.updateRender(message.pos);
        }

        return null;
    }
}