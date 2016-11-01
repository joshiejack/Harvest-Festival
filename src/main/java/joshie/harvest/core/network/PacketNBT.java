package joshie.harvest.core.network;


import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public abstract class PacketNBT extends PenguinPacket {
    protected NBTTagCompound tag;

    public PacketNBT() {}
    public PacketNBT(NBTTagCompound tag) {
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
