package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSetPlayerStatus extends PacketSetPlayerNBT {
    private String status;
    private int value;

    public PacketSetPlayerStatus() { super("PenguinStatuses");}
    public PacketSetPlayerStatus(String status, int value) {
        super("PenguinStatuses");
        this.status = status;
        this.value = value;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, status);
        buf.writeInt(value);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        status = ByteBufUtils.readUTF8String(buf);
        value = buf.readInt();
    }

    @Override
    public void setData(NBTTagCompound tag) {
        tag.setInteger(status, value);
    }
}
