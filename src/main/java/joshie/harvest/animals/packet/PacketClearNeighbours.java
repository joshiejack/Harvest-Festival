package joshie.harvest.animals.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.base.tile.TileFillableConnected;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

@Packet(Side.CLIENT)
public class PacketClearNeighbours extends PenguinPacket {
    private BlockPos pos;

    public PacketClearNeighbours() {}
    public PacketClearNeighbours(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        pos = BlockPos.fromLong(from.readLong());
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos offset = pos.add(x, 0, z);
                TileEntity tile = player.world.getTileEntity(offset);
                if (tile instanceof TileFillableConnected) {
                    (((TileFillableConnected)tile)).resetClientData();
                }
            }
        }
    }
}
