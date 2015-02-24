package joshie.harvestmoon.core.handlers.events;

import static joshie.harvestmoon.core.network.PacketHandler.sendToClient;
import joshie.harvestmoon.core.config.Calendar;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.helpers.PlayerHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.core.network.PacketSetCalendar;
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
        }
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.phase != Phase.END) return;
        World world = MinecraftServer.getServer().getEntityWorld();
        if (world.getWorldTime() % Calendar.TICKS_PER_DAY == 1) {
            (new Thread(HMModInfo.CAPNAME + " Calendar Thread") {
                @Override
                public void run() {
                    CalendarHelper.newDay();
                }
            }).start();
        }
    }
}
