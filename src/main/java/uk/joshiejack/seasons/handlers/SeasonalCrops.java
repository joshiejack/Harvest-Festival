package uk.joshiejack.seasons.handlers;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class SeasonalCrops {
    public static final Map<Block, SeasonHandler> REGISTRY = Maps.newHashMap();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCropGrow(BlockEvent.CropGrowEvent.Pre event) {
        SeasonHandler handler = REGISTRY.get(event.getState().getBlock());
        if (handler != null && !handler.isValidSeason(event.getWorld(), event.getPos())) {
            event.setResult(Event.Result.DENY);
        }
    }
}
