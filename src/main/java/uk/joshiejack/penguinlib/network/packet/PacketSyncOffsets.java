package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.tile.inventory.TileConnectable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncOffsets extends PacketRenderBlockUpdate {
    private BlockPos pos;
    private int offsetX;
    private int offsetZ;

    public PacketSyncOffsets() {}
    public PacketSyncOffsets(BlockPos pos, int offsetX, int offsetZ) {
        super(pos);
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        to.writeInt(offsetX);
        to.writeInt(offsetZ);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        offsetX = from.readInt();
        offsetZ = from.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileConnectable) {
            ((TileConnectable)tile).setOffsets(offsetX, offsetZ);
        }

        super.handlePacket(player);
    }
}
