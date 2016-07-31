package joshie.harvest.animals.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.base.TileFillable;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.PenguinPacketLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketSyncFilled extends PenguinPacketLocation {
    private int isFilled;

    public PacketSyncFilled() {}
    public PacketSyncFilled(TileEntity tile, int isFilled) {
        super(tile);
        this.isFilled = isFilled;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeByte(isFilled);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        isFilled = buffer.readByte();
    }

    public void handlePacket(EntityPlayer player) {
        TileEntity tile = MCClientHelper.getTile(this);
        if (tile instanceof TileFillable) {
            ((TileFillable) tile).setFilled(isFilled);
            MCClientHelper.updateRender(pos);
        }
    }
}