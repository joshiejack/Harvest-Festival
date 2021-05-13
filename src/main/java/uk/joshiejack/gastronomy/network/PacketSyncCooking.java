package uk.joshiejack.gastronomy.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.gastronomy.tile.base.TileCooking;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncCooking extends PenguinPacket {
    private BlockPos pos;
    private boolean isCooking;
    private int cookTimer;

    public PacketSyncCooking() {}
    public PacketSyncCooking(BlockPos pos, boolean isCooking, int cookTimer) {
        this.pos = pos;
        this.isCooking = isCooking;
        this.cookTimer = cookTimer;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeLong(pos.toLong());
        to.writeBoolean(isCooking);
        to.writeByte(cookTimer);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        pos = BlockPos.fromLong(from.readLong());
        isCooking = from.readBoolean();
        cookTimer = from.readByte();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileCooking) {
            (((TileCooking)tile)).setCooking(isCooking, cookTimer);
        }
    }
}
