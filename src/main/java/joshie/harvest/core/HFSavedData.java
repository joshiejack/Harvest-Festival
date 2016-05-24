package joshie.harvest.core;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.tick.TickDailyServer;
import joshie.harvest.crops.CropTrackerServer;
import joshie.harvest.npc.town.TownTrackerServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class HFSavedData extends WorldSavedData {
    public static final String DATA_NAME = HFModInfo.CAPNAME + "-Data";

    private CalendarServer calendar = new CalendarServer();
    private AnimalTrackerServer animals = new AnimalTrackerServer();
    private CropTrackerServer crops = new CropTrackerServer();
    private TickDailyServer ticking = new TickDailyServer();
    private TownTrackerServer towns = new TownTrackerServer();

    public HFSavedData(String string) {
        super(string);
    }

    public void setWorld(World world) {
        calendar.setWorld(world);
    }

    public AnimalTrackerServer getAnimalTracker() {
        return animals;
    }

    public CalendarServer getCalendar() {
        return calendar;
    }

    public CropTrackerServer getCropTracker() {
        return crops;
    }

    public TickDailyServer getTickables() {
        return ticking;
    }

    public TownTrackerServer getTownTracker() {
        return towns;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        calendar.readFromNBT(nbt.getCompoundTag("Calendar"));
        crops.readFromNBT(nbt.getCompoundTag("CropTracker"));
        ticking.readFromNBT(nbt.getCompoundTag("TickingDaily"));
        towns.readFromNBT(nbt.getCompoundTag("TownTracker"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Calendar", calendar.writeToNBT(new NBTTagCompound()));
        nbt.setTag("CropTracker", crops.writeToNBT(new NBTTagCompound()));
        nbt.setTag("TickingDaily", ticking.writeToNBT(new NBTTagCompound()));
        nbt.setTag("TownTracker", towns.writeToNBT(new NBTTagCompound()));
        return nbt;
    }
}