package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.tile.inventory.TileConnectable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSurroundingsChanged extends PenguinPacket {
    private BlockPos pos;

    public PacketSurroundingsChanged() {}
    public PacketSurroundingsChanged(BlockPos pos) {
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
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileConnectable) {
            (((TileConnectable)tile)).onSurroundingsChanged();
        }
    }
}
