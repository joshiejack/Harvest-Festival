package joshie.harvestmoon.handlers.events;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.network.PacketHandler.sendToClient;
import joshie.harvestmoon.network.PacketSetCalendar;
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
        handler.getServer().getPlayerData(event.player).setBirthday();
        EntityPlayer player = event.player;
        if (player instanceof EntityPlayerMP) {
            sendToClient(new PacketSetCalendar(handler.getServer().getCalendar().getDate()), (EntityPlayerMP) player);
            PlayerDataServer data = handler.getServer().getPlayerData(player);
            data.syncPlayerStats();
            data.getQuests().syncQuests();
        }
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
        if (event.phase != Phase.START) return;
        World world = MinecraftServer.getServer().getEntityWorld();
        if (world.getWorldTime() % 24000L == 1) {
            handler.getServer().getCalendar().newDay();
        }
    }
}
