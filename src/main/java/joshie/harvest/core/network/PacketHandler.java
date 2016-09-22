package joshie.harvest.core.network;

import joshie.harvest.core.base.tile.TileHarvest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class PacketHandler {
    private static final PenguinNetwork INSTANCE = new PenguinNetwork(MODID);
    public static void registerPacket(Class clazz) {
        registerPacket(clazz, Side.CLIENT);
        registerPacket(clazz, Side.SERVER);
    }

    public static void registerPacket(Class clazz, Side side) {
        INSTANCE.registerPacket(clazz, side);
    }

    public static void sendToClient(IMessage message, EntityPlayer player) {
        INSTANCE.sendToClient(message, (EntityPlayerMP) player);
    }

    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }

    public static void sendToEveryone(IMessage message) {
        INSTANCE.sendToEveryone(message);
    }

    public static void sendToDimension(int dimension, IMessage message) {
        INSTANCE.sendToDimension(dimension, message);
    }

    public static void sendToAllAround(IMessage packet, int dim, double x, double y, double z, int distance) {
        INSTANCE.sendToAllAround(packet, dim, x, y, z, distance);
    }

    public static void sendRefreshPacket(TileHarvest tile) {
        tile.hasChanged = true;
        Packet<?> pkt = tile.getUpdatePacket();
        if (pkt != null) {
            for (EntityPlayer player: tile.getWorld().playerEntities) {
                ((EntityPlayerMP) player).connection.sendPacket(pkt);
            }
        }

        tile.hasChanged = false;
    }
}