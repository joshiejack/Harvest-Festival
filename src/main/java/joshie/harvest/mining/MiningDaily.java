package joshie.harvest.mining;

import joshie.harvest.api.ticking.IDailyTickable.Phase;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@HFEvents
public class MiningDaily {
    @SubscribeEvent
    public void onChunkLoad(ChunkDataEvent.Load event) {
        if (event.getWorld().provider.getDimension() == HFMining.MINING_ID) {
            NBTTagCompound tag = getCompoundTag(event.getData());
            long chunk = ChunkPos.chunkXZ2Int(event.getChunk().xPosition, event.getChunk().zPosition);
            if (tag.hasKey("" + chunk)) {
                int days = CalendarHelper.getElapsedDays(event.getWorld().getWorldTime());
                int lastDay = tag.getInteger("" + chunk);
                if (days - lastDay > 0) {
                    HFTrackers.getTickables(event.getWorld()).newDay(Phase.MINE);
                }
            }
        }
    }

    @SubscribeEvent
    public void onChunkSave(ChunkDataEvent.Save event) {
        if (event.getWorld().provider.getDimension() == HFMining.MINING_ID) {
            NBTTagCompound tag = getCompoundTag(event.getData());
            long chunk = ChunkPos.chunkXZ2Int(event.getChunk().xPosition, event.getChunk().zPosition);
            int days = CalendarHelper.getElapsedDays(event.getWorld().getWorldTime());
            tag.setInteger("" + chunk, days);
        }
    }

    private NBTTagCompound getCompoundTag(NBTTagCompound tag) {
        if (tag.hasKey("LastTickData")) return tag.getCompoundTag("LastTickData");
        else {
            NBTTagCompound data = new NBTTagCompound();
            tag.setTag("LastTickData", data);
            return data;
        }
    }
}
