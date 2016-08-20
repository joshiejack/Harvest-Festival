package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.calendar.packets.PacketSetCalendar;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.MCServerHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.TownTrackerServer;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

@HFEvents
public class EventsHandler {
    @HFEvents
    public static class ClientReset {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void onOpenGui(GuiOpenEvent event) {
            if (event.getGui() instanceof GuiWorldSelection || event.getGui() instanceof GuiMultiplayer) {
                HFTrackers.resetClient();
            }
        }
    }

    public File getFolder(File playerDir) {
        File dir = new File(playerDir, "HF");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    @SubscribeEvent
    public void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        PlayerTrackerServer data = new PlayerTrackerServer((EntityPlayerMP)event.getEntityPlayer());
        File file = new File(getFolder(event.getPlayerDirectory()), UUIDHelper.getPlayerUUID(event.getEntityPlayer()) + ".dat");
        if (!file.exists()) file = new File(getFolder(event.getPlayerDirectory()), UUIDHelper.getLastKnownUUID(event.getEntityPlayer()) + ".dat");
        if (file.exists()) {
            try {
                FileInputStream fileinputstream = new FileInputStream(file);
                NBTTagCompound tag = CompressedStreamTools.readCompressed(fileinputstream);
                fileinputstream.close();
                data.readFromNBT(tag);
            } catch (Exception e) { e.printStackTrace(); }
        }

        HFTrackers.setPlayerData(event.getEntityPlayer(), data);
    }

    //Setup the Player
    @SubscribeEvent
    public void onPlayerSave(PlayerEvent.SaveToFile event) {
        try {
            PlayerTrackerServer data = HFTrackers.getPlayerTracker(event.getEntityPlayer());
            File file = new File(getFolder(event.getPlayerDirectory()), UUIDHelper.getPlayerUUID(event.getEntityPlayer()) + ".dat");
            NBTTagCompound tag = data.writeToNBT(new NBTTagCompound());
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            CompressedStreamTools.writeCompressed(tag, fileoutputstream);
            fileoutputstream.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    //Server tick for new day
    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.phase != Phase.END) return;
        for (World world: FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) {
            if (world != null) {
                if (world.getWorldTime() % TICKS_PER_DAY == 0) {
                    newDay(world); //Perform everything
                    if (world.provider.getDimension() == 0) { //If it's the overworld, tick the player trackers
                        for (PlayerTrackerServer player : HFTrackers.getPlayerTrackers()) {
                            player.newDay(CalendarHelper.getTime(world));
                        }
                    }
                }
            }
        }
    }

    //New day
    public static void newDay(final World world) {
        HFTrackers.<CalendarServer>getCalendar(world).newDay();
        HFTrackers.getTickables(world).newDay();
        HFTrackers.<AnimalTrackerServer>getAnimalTracker(world).newDay();
        HFTrackers.<TownTrackerServer>getTownTracker(world).newDay();
        HFTrackers.markDirty(world);
    }

    //Sync data on login
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            HFTrackers.getPlayerTracker(player).getStats().setBirthday(FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0]); //Set birthday to overworld date
            PacketHandler.sendToDimension(player.worldObj.provider.getDimension(), new PacketSetCalendar(HFTrackers.getCalendar(player.worldObj).getDate()));
            HFTrackers.<TownTrackerServer>getTownTracker(event.player.worldObj).syncToPlayer(player);
            PlayerTrackerServer data = HFTrackers.getPlayerTracker(player);
            data.syncPlayerStats(player);
        }
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            World world = MCServerHelper.getWorld(event.toDim);
            PacketHandler.sendToDimension(event.toDim, new PacketSetCalendar(HFTrackers.getCalendar(world).getDate()));
            HFTrackers.<TownTrackerServer>getTownTracker(world).syncToPlayer(event.player); //Resync the town data
        }
    }
}