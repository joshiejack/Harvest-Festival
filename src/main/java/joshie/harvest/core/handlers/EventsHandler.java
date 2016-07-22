package joshie.harvest.core.handlers;

import joshie.harvest.api.HFRegister;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.helpers.generic.MCServerHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.player.PlayerTrackerServer;
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

@HFRegister(data = "events")
public class EventsHandler {
    //Setup the Client
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiWorldSelection || event.getGui() instanceof GuiMultiplayer) {
            HFTrackers.resetClient();
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
            PlayerTrackerServer data = HFTrackers.getServerPlayerTracker(event.getEntityPlayer());
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
        HFTrackers.getCalendar(world).newDay();
        HFTrackers.getTickables(world).newDay();
        HFTrackers.getAnimalTracker(world).newDay();
        HFTrackers.getTownTracker(world).newDay();
        HFTrackers.markDirty(world);
    }

    //Sync data on login
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            HFTrackers.getPlayerTracker(player).getStats().setBirthday(FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0]); //Set birthday to overworld date
            PacketHandler.sendToClient(new PacketSetCalendar(player.worldObj.provider.getDimension(), HFTrackers.getCalendar(player.worldObj).getDate()), player);
            HFTrackers.getTownTracker(event.player.worldObj).syncToPlayer(player);
            PlayerTrackerServer data = HFTrackers.getServerPlayerTracker(player);
            data.syncPlayerStats(player);
        }
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            PacketHandler.sendToClient(new PacketSetCalendar(event.toDim, HFTrackers.getCalendar(MCServerHelper.getWorld(event.toDim)).getDate()), event.player);
        }
    }
}