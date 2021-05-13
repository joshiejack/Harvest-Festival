package uk.joshiejack.penguinlib.network.packet;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.client.PenguinTeamsClient;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncTeamMembers extends PenguinPacket {
    private Map<UUID, UUID> memberOf;

    public PacketSyncTeamMembers() {}
    public PacketSyncTeamMembers(Map<UUID, UUID> memberOf) {
        this.memberOf = memberOf;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeByte(memberOf.size());
        memberOf.forEach((key, value) -> {
            ByteBufUtils.writeUTF8String(to, key.toString());
            ByteBufUtils.writeUTF8String(to, value.toString());
        });
    }

    @Override
    public void fromBytes(ByteBuf from) {
        memberOf = Maps.newHashMap();
        int size = from.readByte();
        IntStream.range(0, size).forEach(i -> {
            memberOf.put(UUID.fromString(ByteBufUtils.readUTF8String(from)), UUID.fromString(ByteBufUtils.readUTF8String(from)));
        });
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        PenguinTeamsClient.setMembers(memberOf);
    }
}
