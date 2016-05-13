package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerClient;
import joshie.harvest.calendar.CalendarClient;
import joshie.harvest.crops.CropTrackerClient;
import joshie.harvest.mining.MineTrackerClient;
import joshie.harvest.npc.town.TownTrackerClient;
import joshie.harvest.player.PlayerTrackerClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class ClientHandler extends SideHandler {
    private CalendarClient calendar;
    private AnimalTrackerClient animals;
    private CropTrackerClient crops;
    private MineTrackerClient mine;
    private PlayerTrackerClient player;
    private TownTrackerClient town;
    
    public ClientHandler() {
        calendar = new CalendarClient();
        animals = new AnimalTrackerClient();
        crops = new CropTrackerClient();
        mine = new MineTrackerClient();
        player = new PlayerTrackerClient();
        town = new TownTrackerClient();
    }

    @Override
    public CalendarClient getCalendar() {
        return calendar;
    }

    @Override
    public AnimalTrackerClient getAnimalTracker() {
        return animals;
    }

    @Override
    public CropTrackerClient getCropTracker() {
        return crops;
    }
    
    @Override
    public MineTrackerClient getMineTracker() {
        return mine;
    }

    @Override
    public TownTrackerClient getTownTracker() {
        return town;
    }
    
    public PlayerTrackerClient getPlayerTracker() {
        return player;
    }
    
    @Override
    public PlayerTrackerClient getPlayerTracker(EntityPlayer player) {
        return getPlayerTracker();
    }

    @Override
    public PlayerTrackerClient getPlayerTracker(UUID uuid) {
        return getPlayerTracker();
    }
}
