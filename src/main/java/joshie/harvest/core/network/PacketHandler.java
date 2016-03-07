package joshie.harvest.core.network;

import static joshie.harvest.core.config.HFConfig.PACKET_DISTANCE;
import static joshie.harvest.core.lib.HFModInfo.MODID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {
    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
    private static int id;

    public static void registerPacket(Class clazz, Side side) {
        INSTANCE.registerMessage(clazz, clazz, id++, side);
    }

    public static void sendToEveryone(IMessage packet) {
        INSTANCE.sendToAll(packet);
    }

    public static void sendToClient(IMessage packet, EntityPlayerMP player) {
        INSTANCE.sendTo(packet, player);
    }

    public static void sendAround(IMessage packet, int dim, double x, double y, double z) {
        INSTANCE.sendToAllAround(packet, new TargetPoint(dim, x, y, z, PACKET_DISTANCE));
    }

    public static void sendToServer(IMessage packet) {
        INSTANCE.sendToServer(packet);
    }

    public static Packet getPacket(IMessage packet) {
        return INSTANCE.getPacketFrom(packet);
    }

    public static void sendAround(IMessage packet, TileEntity tile) {
        sendAround(packet, tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord);
    }
}