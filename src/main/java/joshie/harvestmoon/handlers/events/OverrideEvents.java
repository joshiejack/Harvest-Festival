package joshie.harvestmoon.handlers.events;

import joshie.harvestmoon.init.HMBlocks;
import net.minecraftforge.client.event.TextureStitchEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OverrideEvents {
    @SubscribeEvent
    public void onPreStitch(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 0) {
            HMBlocks.snow.registerBlockIcons(event.map);
            HMBlocks.farmland.registerBlockIcons(event.map);
        }
    }
}
