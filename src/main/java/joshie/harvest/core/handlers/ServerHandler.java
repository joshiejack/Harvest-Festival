package joshie.harvest.core.handlers;

import joshie.harvest.animals.tracker.AnimalTrackerServer;
import joshie.harvest.core.HFSavedData;
import joshie.harvest.core.util.handlers.SideHandler;
import net.minecraft.world.World;

public class ServerHandler extends SideHandler {
    private HFSavedData data;

    public ServerHandler(World world) {
        data = (HFSavedData) world.getPerWorldStorage().getOrLoadData(HFSavedData.class, HFSavedData.DATA_NAME);
        if (data == null) {
            data = new HFSavedData(HFSavedData.DATA_NAME);
            world.getPerWorldStorage().setData(HFSavedData.DATA_NAME, data);
        }
    }

    @Override
    public void setWorld(World world) {
        data.setWorld(world);
    }

    @Override
    public AnimalTrackerServer getAnimalTracker() {
        return data.getAnimalTracker();
    }

    public DailyTickHandler getTickables() {
        return data.getTickables();
    }
}
