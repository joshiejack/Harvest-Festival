package uk.joshiejack.penguinlib.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.penguinlib.util.helpers.minecraft.PlayerHelper;

import javax.annotation.Nullable;
import java.util.UUID;

@SuppressWarnings("WeakerAccess, unused")
public class PenguinNetwork {
    private static final PenguinNetwork INSTANCE = new PenguinNetwork(PenguinLib.MOD_ID);
    private final SimpleNetworkWrapper network;
    private final PenguinHandler handler;
    private int id;

    private PenguinNetwork(String name) {
        network = new SimpleNetworkWrapper(name);
        handler = new PenguinHandler();
    }

    public static void registerPacket(Class<? extends PenguinPacket> clazz, Side side) {
        INSTANCE.network.registerMessage(INSTANCE.handler, clazz, INSTANCE.id++, side);
    }

    public static void sendToClient(IMessage message, @Nullable EntityPlayer player) {
        if (player != null) {
            INSTANCE.network.sendTo(message, (EntityPlayerMP) player);
        }
    }

    public static void sendToTeam(IMessage message, World world, UUID uuid) {
        PenguinTeam team = PenguinTeams.getTeamFromID(world, uuid);
        if (team != null) {
            team.members().forEach(member -> {
                EntityPlayer player = PlayerHelper.getPlayerFromUUID(world, member);
                if (player != null) {
                    PenguinNetwork.sendToClient(message, player);
                }
            });
        }
    }

    public static void sendToServer(IMessage message) {
        INSTANCE.network.sendToServer(message);
    }

    public static void sendToEveryone(IMessage message) {
        INSTANCE.network.sendToAll(message);
    }

    public static void sendToDimension(int dimension, IMessage message) {
        INSTANCE.network.sendToDimension(message, dimension);
    }

    public static void sendToAllAround(IMessage packet, int dim, double x, double y, double z, int distance) {
        INSTANCE.network.sendToAllAround(packet, new TargetPoint(dim, x, y, z, distance));
    }

    public static void sendToNearby(WorldServer world, BlockPos pos, IMessage message) {
        if (world.isChunkGeneratedAt(pos.getX() >> 4, pos.getZ() >> 4)) {
            Chunk chunk = world.getChunk(pos);
            for (EntityPlayer player : world.playerEntities) {
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP mp = (EntityPlayerMP) player;
                    if (world.getPlayerChunkMap().isPlayerWatchingChunk(mp, chunk.x, chunk.z)) {
                        sendToClient(message, mp);
                    }
                }
            }
        }
    }

    public static void sendToNearby(Entity entity, IMessage message) {
        sendToNearby((WorldServer)entity.world, new BlockPos(entity), message);
    }

    public static void sendToNearby(TileEntity tile, IMessage message) {
        sendToNearby((WorldServer) tile.getWorld(), tile.getPos(), message);
    }

    public Packet<?> getPacketFrom(IMessage packet) {
        return network.getPacketFrom(packet);
    }


}
