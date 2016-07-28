package joshie.harvest.core.render;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import net.minecraftforge.event.terraingen.BiomeEvent.GetFoliageColor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {
    //Orange Leaves in Autumn
    @SubscribeEvent
    public void getFoliageColor(GetFoliageColor event) {
        if (HFTrackers.getCalendar(MCClientHelper.getWorld()).getDate().getSeason() == Season.AUTUMN) {
            event.setNewColor(0xFF9900);
        }
    }
}