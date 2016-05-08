package joshie.harvest.core.network;

import joshie.harvest.core.network.penguin.PenguinNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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

    public static void sendAround(IMessage packet, int dim, double x, double y, double z) {
        INSTANCE.sendToAllAround(packet, dim, x, y, z);
    }

    public static Packet<?> getPacket(IMessage packet) {
        return INSTANCE.getPacketFrom(packet);
    }

    public static void sendAround(IMessage packet, TileEntity tile) {
        BlockPos pos = tile.getPos(); //Damn you!
        sendAround(packet, tile.getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ());
    }
}