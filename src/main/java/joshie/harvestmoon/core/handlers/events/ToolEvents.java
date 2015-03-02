package joshie.harvestmoon.core.handlers.events;

import joshie.harvestmoon.init.HMConfiguration;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToolEvents {
    @SubscribeEvent
    public void onUseHoe(UseHoeEvent event) {
        if (HMConfiguration.vanilla.HOES_ARE_USELESS) {
            event.setCanceled(true);
        }
    }
}
