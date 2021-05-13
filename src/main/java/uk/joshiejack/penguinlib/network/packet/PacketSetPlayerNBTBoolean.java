package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import joptsimple.internal.Strings;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PacketSetPlayerNBTBoolean extends PacketSetPlayerNBT {
    private final String dataName;
    protected boolean isTrue;

    public PacketSetPlayerNBTBoolean() {
        super(Strings.EMPTY);
        this.dataName = Strings.EMPTY;
    }

    public PacketSetPlayerNBTBoolean(String tagName, String dataName) {
        super(tagName);
        this.dataName = dataName;
    }

    public PacketSetPlayerNBTBoolean(String tagName, String dataName, boolean isTrue) {
        super(tagName);
        this.isTrue = isTrue;
        this.dataName = dataName;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isTrue);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isTrue = buf.readBoolean();
    }

    @Override
    public void setData(NBTTagCompound tag) {
        tag.setBoolean(dataName, isTrue);
    }
}
