package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public abstract class PacketSyncNBTTagCompound extends PenguinPacket {
    protected NBTTagCompound tag;

    public PacketSyncNBTTagCompound() {}
    public PacketSyncNBTTagCompound(NBTTagCompound tag) {
        this.tag = tag;
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeTag(to, tag);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        tag = ByteBufUtils.readTag(from);
    }
}
