package joshie.harvestmoon.network;

import static joshie.harvestmoon.Configuration.PACKET_DISTANCE;
import static joshie.harvestmoon.lib.HMModInfo.MODID;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

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
}