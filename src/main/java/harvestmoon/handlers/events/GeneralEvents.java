package harvestmoon.handlers.events;

import static harvestmoon.HarvestMoon.handler;
import static harvestmoon.helpers.CalendarHelper.getSeason;
import harvestmoon.calendar.Season;
import harvestmoon.handlers.ServerHandler;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

public class GeneralEvents {    
    @SubscribeEvent
    public void onLoad(WorldEvent.Load event) {
        if (event.world.provider.dimensionId == 0) {
            if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
                handler.setServer(new ServerHandler(event.world));
            }
        }
    }
    
    @SubscribeEvent
    public void getFoliageColor(GetFoliageColor event) {
        if(getSeason() == Season.AUTUMN) {
            event.newColor = 0xFF9900;
        }
    }
}
