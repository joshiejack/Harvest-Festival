package uk.joshiejack.economy.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.penguinlib.network.PenguinPacket;

import java.util.Objects;

public abstract class AbstractPacketSyncDepartment extends PenguinPacket {
    protected Department department;

    public AbstractPacketSyncDepartment() {}
    public AbstractPacketSyncDepartment(Department department) {
        this.department = department;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, Objects.requireNonNull(department.id()));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        department = Department.REGISTRY.get(ByteBufUtils.readUTF8String(buf));
    }
}
