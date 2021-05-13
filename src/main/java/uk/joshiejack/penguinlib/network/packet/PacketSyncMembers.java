package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.tile.inventory.TileConnectable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncMembers extends PacketRenderBlockUpdate {
    private BlockPos pos;
    private int members;

    public PacketSyncMembers() {}
    public PacketSyncMembers(BlockPos pos, int members) {
        super(pos);
        this.members = members;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        to.writeInt(members);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        members = from.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileConnectable) {
            ((TileConnectable)tile).setMembers(members);
        }

        super.handlePacket(player);
    }
}
