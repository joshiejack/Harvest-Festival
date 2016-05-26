package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.HFSavedData;
import joshie.harvest.crops.CropTrackerServer;
import joshie.harvest.npc.town.TownTrackerServer;
import net.minecraft.world.World;

public class ServerHandler extends SideHandler {
    private HFSavedData data;

    public ServerHandler(World world) {
        data = (HFSavedData) world.getPerWorldStorage().getOrLoadData(HFSavedData.class, HFSavedData.DATA_NAME);
        if (data == null) {
            data = new HFSavedData(HFSavedData.DATA_NAME);
            world.getPerWorldStorage().setData(HFSavedData.DATA_NAME, data);
        }

        data.setWorld(world);
    }

    @Override
    public AnimalTrackerServer getAnimalTracker() {
        return data.getAnimalTracker();
    }

    @Override
    public CalendarServer getCalendar() {
        return data.getCalendar();
    }

    @Override
    public CropTrackerServer getCropTracker() {
        return data.getCropTracker();
    }

    public TickDailyServer getTickables() {
        return data.getTickables();
    }

    @Override
    public TownTrackerServer getTownTracker() {
        return data.getTownTracker();
    }
    
    public void markDirty() {
        data.markDirty();
    }
}
