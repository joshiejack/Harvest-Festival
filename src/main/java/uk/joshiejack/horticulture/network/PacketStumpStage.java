package uk.joshiejack.horticulture.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.horticulture.tileentity.TileStump;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

@SuppressWarnings("unused")
@PenguinLoader(side = Side.CLIENT)
public class PacketStumpStage extends PenguinPacket {
    private BlockPos pos;
    private int stage;

    public PacketStumpStage() {}
    public PacketStumpStage(BlockPos pos, int stage) {
        this.pos = pos;
        this.stage = stage;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeLong(pos.toLong());
        to.writeByte(stage);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        pos = BlockPos.fromLong(from.readLong());
        stage = from.readByte();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileStump) {
            ((TileStump)tile).setStage(stage);
        }
    }
}
