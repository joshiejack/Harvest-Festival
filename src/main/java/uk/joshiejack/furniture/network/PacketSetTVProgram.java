package uk.joshiejack.furniture.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.furniture.television.TVProgram;
import uk.joshiejack.furniture.tile.TileTelevision;
import uk.joshiejack.penguinlib.network.packet.PacketSendPenguin;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.CLIENT)
public class PacketSetTVProgram extends PacketSendPenguin<TVProgram> {
    private BlockPos pos;

    public PacketSetTVProgram() { super(TVProgram.REGISTRY); }
    public PacketSetTVProgram(BlockPos pos, TVProgram program) {
        super(TVProgram.REGISTRY, program);
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        to.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        pos = BlockPos.fromLong(from.readLong());
    }

    @Override
    public void handlePacket(EntityPlayer player, TVProgram program) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileTelevision) {
            ((TileTelevision)tile).setProgram(program);
        }
    }
}

