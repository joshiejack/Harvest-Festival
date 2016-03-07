package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class AbstractPacketOrientation extends AbstractPacketLocation {
    private ForgeDirection dir;

    public AbstractPacketOrientation() {}
    public AbstractPacketOrientation(int dim, int x, int y, int z, ForgeDirection dir) {
        super(dim, x, y, z);
        this.dir = dir == null ? ForgeDirection.NORTH : dir;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeInt(dir.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        dir = ForgeDirection.getOrientation(buffer.readInt());
    }

    public IMessage onMessage(AbstractPacketOrientation message, MessageContext ctx) {
        EntityPlayer player = MCClientHelper.getPlayer();
        TileEntity tile = MCClientHelper.getTile(message);
        if (tile instanceof IFaceable) {
            ((IFaceable) player.worldObj.getTileEntity(message.x, message.y, message.z)).setFacing(message.dir);
            MCClientHelper.updateRender(message.x, message.y, message.z);
        }

        return null;
    }
}