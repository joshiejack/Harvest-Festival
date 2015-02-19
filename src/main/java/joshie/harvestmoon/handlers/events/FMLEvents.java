package joshie.harvestmoon.handlers.events;

import static joshie.harvestmoon.network.PacketHandler.sendToClient;
import joshie.harvestmoon.config.Calendar;
import joshie.harvestmoon.helpers.CalendarHelper;
import joshie.harvestmoon.helpers.PlayerHelper;
import joshie.harvestmoon.network.PacketSetCalendar;
import joshie.harvestmoon.network.PacketSyncFridge;
import joshie.harvestmoon.player.PlayerDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
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
            sendToClient(new PacketSetCalendar(CalendarHelper.getServerDate()), (EntityPlayerMP) player);
            PlayerDataServer data = PlayerHelper.getData(player);
            data.syncPlayerStats();
            data.getQuests().syncQuests();
            sendToClient(new PacketSyncFridge(PlayerHelper.getFridge(player)), (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.phase != Phase.START) return;
        World world = MinecraftServer.getServer().getEntityWorld();
        if (world.getWorldTime() % Calendar.TICKS_PER_DAY == 1) {
            (new Thread("HM Calendar Thread") {
                @Override
                public void run() {
                    CalendarHelper.newDay();
                }
            }).start();
        }
    }
}
