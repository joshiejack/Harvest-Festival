package joshie.harvestmoon.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.helpers.generic.ClientHelper;
import joshie.harvestmoon.util.generic.IFaceable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractPacketOrientation extends AbstractPacketLocation {
    ForgeDirection dir;

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
        EntityPlayer player = ClientHelper.getPlayer();
        TileEntity tile = ClientHelper.getTile(message);
        if (tile instanceof IFaceable) {
            ((IFaceable) player.worldObj.getTileEntity(message.x, message.y, message.z)).setFacing(message.dir);
            ClientHelper.updateRender(message.x, message.y, message.z);
        }

        return null;
    }
}