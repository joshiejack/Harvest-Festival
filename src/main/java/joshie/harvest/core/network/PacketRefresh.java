package joshie.harvest.core.network;


import io.netty.buffer.ByteBuf;
import joshie.harvest.core.network.Packet.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Side.CLIENT)
public class PacketRefresh extends PenguinPacket {
    private BlockPos pos;
    private NBTTagCompound tag;

    public PacketRefresh() {}
    public PacketRefresh(BlockPos pos, NBTTagCompound tag) {
        this.pos = pos;
        this.tag = tag;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeLong(pos.toLong());
        ByteBufUtils.writeTag(to, tag);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        pos = BlockPos.fromLong(from.readLong());
        tag = ByteBufUtils.readTag(from);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = player.worldObj.getTileEntity(pos);
        if (tile != null) {
            tile.handleUpdateTag(tag);
            player.worldObj.markBlockRangeForRenderUpdate(pos, pos);
        }
    }
}
