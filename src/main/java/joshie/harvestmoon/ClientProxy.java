package joshie.harvestmoon;

import static joshie.harvestmoon.HarvestMoon.handler;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.entities.EntityNPC;
import joshie.harvestmoon.entities.RenderNPC;
import joshie.harvestmoon.handlers.RenderHandler;
import joshie.harvestmoon.handlers.events.RenderEvents;
import joshie.harvestmoon.lib.RenderIds;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        handler.resetClient();

        for (Crop crop : Crop.crops) {
            try {
                RenderHandler.register(crop.getUnlocalizedName(), Class.forName("joshie.harvestmoon.crops.render." + crop.getRenderName()));
            } catch (Exception e) {}
        }

        RenderIds.ALL = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderHandler());
        FMLCommonHandler.instance().bus().register(new RenderEvents());
        MinecraftForge.EVENT_BUS.register(new RenderEvents());
        RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC());
    }
}
