package harvestmoon;

import harvestmoon.crops.Crop;
import harvestmoon.handlers.RenderHandler;
import harvestmoon.handlers.events.RenderEvents;
import harvestmoon.lib.RenderIds;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxy extends CommonProxy {        
    @Override
    public void init() {
        for(Crop crop: Crop.crops) {
            try {
                RenderHandler.register(crop.getUnlocalizedName(), Class.forName("harvestmoon.crops.render." + crop.getRenderName()));
            } catch (Exception e) {}
        }
        
        RenderIds.ALL = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new RenderHandler());
        FMLCommonHandler.instance().bus().register(new RenderEvents());
        MinecraftForge.EVENT_BUS.register(new RenderEvents());
    }
}
