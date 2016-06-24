package joshie.harvest.core.handlers;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.npc.town.TownTracker;
import joshie.harvest.player.PlayerTracker;
import joshie.harvest.player.PlayerTrackerClient;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class HFTrackers {
    //The Client worlds
    @SideOnly(Side.CLIENT)
    private static TIntObjectMap<SideHandler> CLIENT_WORLDS;
    //The Server worlds
    private static TIntObjectMap<ServerHandler> SERVER_WORLDS = new TIntObjectHashMap<>();

    //The Client player
    @SideOnly(Side.CLIENT)
    private static PlayerTracker CLIENT_PLAYER;

    //Server Players
    private static HashMap<UUID, PlayerTrackerServer> SERVER_PLAYERS = new HashMap<>();

    public static void resetServer() {
        SERVER_WORLDS = new TIntObjectHashMap<>();
    }

    @SideOnly(Side.CLIENT)
    public static void resetClient() {
        CLIENT_WORLDS = new TIntObjectHashMap<>();
        CLIENT_PLAYER = new PlayerTrackerClient();
    }

    @SideOnly(Side.CLIENT)
    private static SideHandler getClient(World world) {
        int dimension = world.provider.getDimension();
        SideHandler handler = CLIENT_WORLDS.get(dimension);
        if (handler == null) {
            handler = new ClientHandler();
            CLIENT_WORLDS.put(dimension, handler);
            handler.setWorld(world);
        }

        return handler;
    }

    private static ServerHandler getServer(World world) {
        ServerHandler handler = SERVER_WORLDS.get(world.provider.getDimension());
        if (handler == null) {
            handler = new ServerHandler(world); //Create a new handler
            SERVER_WORLDS.put(world.provider.getDimension(), handler);
            handler.setWorld(world); //Mark the world for the handler
        }

        return handler;
    }

    private static SideHandler getHandler(World world) {
        return !world.isRemote ? getServer(world) : getClient(world);
    }

    public static Calendar getCalendar(World world) {
        return getHandler(world).getCalendar();
    }

    public static AnimalTracker getAnimalTracker(World world) {
        return getHandler(world).getAnimalTracker();
    }

    public static PlayerTracker getPlayerTracker(EntityPlayer player) {
        return player.worldObj.isRemote ? CLIENT_PLAYER : getServerPlayerTracker(player);
    }

    public static TownTracker getTownTracker(World world) {
        return getHandler(world).getTownTracker();
    }
    
    public static Collection<PlayerTrackerServer> getPlayerTrackers() {
        return SERVER_PLAYERS.values();
    }
    
    @SideOnly(Side.CLIENT)
    public static PlayerTrackerClient getClientPlayerTracker() {
        return (PlayerTrackerClient) CLIENT_PLAYER;
    }
    
    public static PlayerTrackerServer getServerPlayerTracker(EntityPlayer player) {
        return SERVER_PLAYERS.get(UUIDHelper.getPlayerUUID(player));
    }

    public static PlayerTrackerServer getServerPlayerTracker(UUID uuid) {
        return SERVER_PLAYERS.get(uuid);
    }

    public static void setPlayerData(EntityPlayer player, PlayerTrackerServer data) {
        SERVER_PLAYERS.put(UUIDHelper.getPlayerUUID(player), data);
    }

    public static void markDirty(World world) {
        SERVER_WORLDS.get(world.provider.getDimension()).markDirty();
    }

    public static void markDirty(int dimension) {
        SERVER_WORLDS.get(dimension).markDirty();
    }

    public static TickDailyServer getTickables(World world) {
        return getServer(world).getTickables();
    }
}
