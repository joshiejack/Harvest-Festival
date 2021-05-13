package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@PenguinLoader(side = Side.CLIENT)
public abstract class PacketSendPenguin<I extends PenguinRegistry> extends PenguinPacket {
    private final Map<ResourceLocation, I> map;
    private ResourceLocation resource;

    public PacketSendPenguin(Map<ResourceLocation, I> map) {
        this.map = map;
    }
    public PacketSendPenguin(Map<ResourceLocation, I> map, I registry) {
        this.map = map;
        this.resource = registry.getRegistryName();
    }

    @Override
    public void toBytes(ByteBuf to) {
        ByteBufUtils.writeUTF8String(to, resource.toString());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        resource = new ResourceLocation(ByteBufUtils.readUTF8String(from));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        handlePacket(player, map.get(resource));
    }

    public abstract void handlePacket(EntityPlayer player, I object);
}