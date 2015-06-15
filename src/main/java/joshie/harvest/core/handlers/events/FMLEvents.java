package joshie.harvest.core.handlers.events;

import joshie.harvest.core.config.Calendar;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class FMLEvents {
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            PlayerHelper.setBirthday(player, 0, null, 0);
            System.out.println("DATE WAS SENT");
            System.out.println(DataHelper.getCalendar().getDate().getYear());
            PacketHandler.sendToClient(new PacketSetCalendar(DataHelper.getCalendar().getDate()), (EntityPlayerMP) player);
            PlayerTrackerServer data = PlayerHelper.getData(player);
            data.syncPlayerStats();
            data.getQuests().syncQuests();
        }
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.phase != Phase.END) return;
        World world = MinecraftServer.getServer().getEntityWorld();
        if (world.getTotalWorldTime() % Calendar.TICKS_PER_DAY == 0) {
            newDay(false);
        }
    }

    public static void newDay(final boolean forced) {
        int daysPassed = CalendarHelper.getTotalDays(DataHelper.getCalendar().getDate());
        int serverDays = (int) Math.floor(DimensionManager.getWorld(0).getWorldTime() / Calendar.TICKS_PER_DAY);
        if (daysPassed <= serverDays || forced) {
            DataHelper.getCalendar().newDay();
        }
    }
}
