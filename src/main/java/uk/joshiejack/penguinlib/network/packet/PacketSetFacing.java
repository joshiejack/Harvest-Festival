package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSetFacing extends PenguinPacket {
    private BlockPos pos;
    private EnumFacing facing;

    public PacketSetFacing() {}
    public PacketSetFacing(BlockPos pos, EnumFacing facing) {
        this.pos = pos;
        this.facing = facing;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeLong(pos.toLong());
        to.writeByte(facing.ordinal());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        pos = BlockPos.fromLong(from.readLong());
        facing = EnumFacing.values()[from.readByte()];
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof Rotatable) {
            ((Rotatable)tile).setFacing(facing);
        }
    }
}
