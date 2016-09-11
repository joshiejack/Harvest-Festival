package joshie.harvest.core.handlers;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.util.HFEvents;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@HFEvents
public class OfflineTickHandler {
    public static boolean register() { return HFCore.NO_TICK_OFFLINE; }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldTickEvent(WorldTickEvent event) {
        if (event.phase == Phase.END) {
            if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList().size() <= 0) {
                event.world.setWorldTime(event.world.getWorldTime() - 1L);
            }
        }
    }
}
