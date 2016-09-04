package joshie.harvest.core.handlers;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.calendar.CalendarData;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.player.PlayerTracker;
import joshie.harvest.player.PlayerTrackerClient;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.TownTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class HFTrackers {
    /*####################World Based Trackers##########################*/
    @SideOnly(Side.CLIENT)
    private static TIntObjectMap<SideHandler> CLIENT_WORLDS;
    private static TIntObjectMap<ServerHandler> SERVER_WORLDS = new TIntObjectHashMap<>();

    @SideOnly(Side.CLIENT)
    public static void resetClient() {
        CLIENT_WORLDS = new TIntObjectHashMap<>();
        CLIENT_PLAYER = new PlayerTrackerClient();
        CLIENT_CALENDAR = new CalendarClient();
    }

    public static void resetServer() {
        SERVER_WORLDS = new TIntObjectHashMap<>();
        SERVER_CALENDAR = null;
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

    @SuppressWarnings("unchecked")
    public static <A extends AnimalTracker> A getAnimalTracker(World world) {
        return (A) getHandler(world).getAnimalTracker();
    }

    @SuppressWarnings("unchecked")
    public static <T extends TownTracker> T getTownTracker(World world) {
        return (T) getHandler(world).getTownTracker();
    }

    public static TickDailyServer getTickables(World world) {
        return getServer(world).getTickables();
    }

    public static void markDirty(World world) {
        SERVER_WORLDS.get(world.provider.getDimension()).markDirty();
    }

    public static void markDirty(int dimension) {
        SERVER_WORLDS.get(dimension).markDirty();
    }

    /*####################Calendar Trackers##########################*/
    private static final String CALENDAR_NAME = HFModInfo.CAPNAME + "-Calendar";
    @SideOnly(Side.CLIENT)
    private static Calendar CLIENT_CALENDAR;
    private static CalendarServer SERVER_CALENDAR;

    @SuppressWarnings("unchecked")
    public static <C extends Calendar> C getCalendar(World world) {
        return (world.isRemote) ? (C) CLIENT_CALENDAR : (C) getServerCalendar(world);
    }

    private static CalendarServer getServerCalendar(World overworld) {
        if (SERVER_CALENDAR == null) {
            CalendarData data = (CalendarData) overworld.getPerWorldStorage().getOrLoadData(CalendarData.class, CALENDAR_NAME);
            if (data == null) {
                data = new CalendarData(CALENDAR_NAME);
                overworld.getPerWorldStorage().setData(CALENDAR_NAME, data);
            }

            SERVER_CALENDAR = data.getCalendar();
            SERVER_CALENDAR.setWorld(data, overworld);
            SERVER_CALENDAR.recalculate(overworld);
        }

        return SERVER_CALENDAR;
    }

    public static void markCalendarDirty() {
        SERVER_CALENDAR.markDirty();
    }

    /*####################Player Trackers#############################*/
    @SideOnly(Side.CLIENT)
    private static PlayerTracker CLIENT_PLAYER;
    private static HashMap<UUID, PlayerTrackerServer> SERVER_PLAYERS = new HashMap<>();

    public static Collection<PlayerTrackerServer> getPlayerTrackers() {
        return SERVER_PLAYERS.values();
    }
    
    @SideOnly(Side.CLIENT)
    public static PlayerTrackerClient getClientPlayerTracker() {
        return (PlayerTrackerClient) CLIENT_PLAYER;
    }

    public static <P extends PlayerTracker> P getPlayerTrackerFromPlayer(EntityPlayer player) {
        return getPlayerTracker(player.worldObj, UUIDHelper.getPlayerUUID(player));
    }

    @SuppressWarnings("unchecked")
    public static <P extends PlayerTracker> P getPlayerTracker(World world, UUID uuid) {
        return world.isRemote ? (P) CLIENT_PLAYER : (P) SERVER_PLAYERS.get(uuid);
    }

    public static void setPlayerData(EntityPlayer player, PlayerTrackerServer data) {
        SERVER_PLAYERS.put(UUIDHelper.getPlayerUUID(player), data);
    }
}
