package joshie.harvest.core.handlers;

import java.util.Collection;
import java.util.UUID;

import joshie.harvest.animals.AnimalTracker;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.crops.CropTracker;
import joshie.harvest.mining.MineTracker;
import joshie.harvest.player.PlayerTracker;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HFTrackers {
    private static final CalendarServer TEMP_CALENDAR = new CalendarServer();

    @SideOnly(Side.CLIENT)
    private static ClientHandler theClient;
    private static ServerHandler theServer;
    
    public static void reset(World world) {
        if (world != null) {
            theServer = (new ServerHandler(world));
        } else theClient = new ClientHandler();
    }

    private static SideHandler getHandler() {
        return isServer() ? theServer : theClient;
    }

    private static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
    }

    public static Calendar getCalendar() {
        if (isServer()) {
            return theServer == null ? TEMP_CALENDAR : theServer.getCalendar();
        } else return theClient.getCalendar();
    }

    public static AnimalTracker getAnimalTracker() {
        return getHandler().getAnimalTracker();
    }

    public static CropTracker getCropTracker() {
        return getHandler().getCropTracker();
    }

    public static MineTracker getMineTracker() {
        return getHandler().getMineTracker();
    }

    public static PlayerTracker getPlayerTracker(EntityPlayer player) {
        return getHandler().getPlayerTracker(player);
    }

    public static PlayerTracker getPlayerTracker(UUID owner) {
        return getHandler().getPlayerTracker(owner);
    }
    
    public static Collection<PlayerTrackerServer> getPlayerTrackers() {
        return theServer.getPlayerData();
    }
    
    @SideOnly(Side.CLIENT)
    public static PlayerTracker getPlayerTracker() {
        return theClient.getPlayerTracker();
    }

    public static void markDirty() {
        theServer.markDirty();
    }
}
