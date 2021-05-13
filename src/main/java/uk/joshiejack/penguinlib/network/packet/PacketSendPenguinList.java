package uk.joshiejack.penguinlib.network.packet;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Map;

@PenguinLoader(side = Side.CLIENT)
public abstract class PacketSendPenguinList<I extends PenguinRegistry> extends PenguinPacket {
    private final Map<ResourceLocation, I> map;
    private List<ResourceLocation> list;

    public PacketSendPenguinList(Map<ResourceLocation, I> map) {
        this.map = map;
    }
    public PacketSendPenguinList(Map<ResourceLocation, I> map, List<I> registry) {
        this.map = map;
        this.list = Lists.newArrayList();
        registry.forEach((i) -> list.add(i.getRegistryName()));
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(list.size());
        for (ResourceLocation resource: list) {
            ByteBufUtils.writeUTF8String(to, resource.toString());
        }
    }

    @Override
    public void fromBytes(ByteBuf from) {
        list = Lists.newArrayList();
        int times = from.readInt();
        for (int i = 0; i < times; i++) {
            list.add(new ResourceLocation(ByteBufUtils.readUTF8String(from)));
      }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        List<I> i = Lists.newArrayList();
        list.forEach((r) -> i.add(map.get(r)));
        handlePacket(player, i);
    }

    public abstract void handlePacket(EntityPlayer player, List<I> object);
}