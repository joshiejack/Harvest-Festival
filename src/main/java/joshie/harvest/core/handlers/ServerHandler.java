package joshie.harvest.core.handlers;

import java.util.Collection;
import java.util.UUID;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.HFSavedData;
import joshie.harvest.crops.CropTrackerServer;
import joshie.harvest.mining.MineTrackerServer;
import joshie.harvest.player.PlayerTrackerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class ServerHandler extends SideHandler {
    private HFSavedData data;

    public ServerHandler(World world) {
        data = (HFSavedData) world.loadItemData(HFSavedData.class, HFSavedData.DATA_NAME);
        if (data == null) {
            data = new HFSavedData(HFSavedData.DATA_NAME);
            world.setItemData(HFSavedData.DATA_NAME, data);
        }
    }
    
    public Collection<PlayerTrackerServer> getPlayerData() {
        return data.getPlayerData();
    }
    
    @Override
    public CalendarServer getCalendar() {
        return data.getCalendar();
    }

    @Override
    public AnimalTrackerServer getAnimalTracker() {
        return data.getAnimalTracker();
    }

    @Override
    public CropTrackerServer getCropTracker() {
        return data.getCropTracker();
    }
    
    @Override
    public MineTrackerServer getMineTracker() {
        return data.getMineTracker();
    }
    
    @Override
    public PlayerTrackerServer getPlayerTracker(EntityPlayer player) {
        return data.getPlayerData((EntityPlayerMP) player);
    }
    
    @Override
    public PlayerTrackerServer getPlayerTracker(UUID uuid) {
        return data.getPlayerData(uuid);
    }
    
    public void markDirty() {
        data.markDirty();
    }
}
