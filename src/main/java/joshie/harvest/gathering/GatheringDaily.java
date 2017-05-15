package joshie.harvest.gathering;

import com.google.common.cache.CacheBuilder;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@HFEvents
@SuppressWarnings("unused")
public class GatheringDaily {
    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        if (event.getWorld().provider.getDimension() == 0) {
            NBTTagCompound tag = NBTHelper.getLastTickData(event.getData());
            long chunk = ChunkPos.asLong(event.getChunk().xPosition, event.getChunk().zPosition);
            if (tag.hasKey("" + chunk)) {
                int days = CalendarHelper.getElapsedDays(event.getWorld().getWorldTime());
                int lastDay = tag.getInteger("" + chunk);
                if (days - lastDay > 0) {
                    TownDataServer data = TownHelper.getClosestTownToBlockPos(event.getWorld(), new BlockPos(event.getChunk().xPosition * 16, 64, event.getChunk().zPosition * 16), false);
                    if (data != HFTrackers.getTowns(event.getWorld()).getNullTown()) {
                        data.gathering.newDay(event.getWorld(), data.getTownCentre(), data.getBuildings(), CacheBuilder.newBuilder().build());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (event.getWorld().provider.getDimension() == 0) {
            NBTTagCompound tag = NBTHelper.getLastTickData(event.getData());
            long chunk = ChunkPos.asLong(event.getChunk().xPosition, event.getChunk().zPosition);
            int days = CalendarHelper.getElapsedDays(event.getWorld().getWorldTime());
            tag.setInteger("" + chunk, days);
        }
    }
}
