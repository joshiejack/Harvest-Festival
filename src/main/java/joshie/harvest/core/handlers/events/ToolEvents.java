package joshie.harvest.core.handlers.events;

import joshie.harvest.init.HFConfig;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToolEvents {
    @SubscribeEvent
    public void onUseHoe(UseHoeEvent event) {
        if (HFConfig.vanilla.HOES_ARE_USELESS) {
            event.setCanceled(true);
        }
    }
}
