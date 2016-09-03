package joshie.harvest.core;

import joshie.harvest.animals.AnimalTrackerServer;
import joshie.harvest.core.handlers.TickDailyServer;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.town.TownTrackerServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class HFSavedData extends WorldSavedData {
    public static final String DATA_NAME = HFModInfo.CAPNAME + "-Data";

    private AnimalTrackerServer animals = new AnimalTrackerServer();
    private TickDailyServer ticking = new TickDailyServer();
    private TownTrackerServer towns = new TownTrackerServer();

    public HFSavedData(String string) {
        super(string);
    }

    public void setWorld(World world) {
        animals.setWorld(world);
        ticking.setWorld(world);
        towns.setWorld(world);
    }

    public AnimalTrackerServer getAnimalTracker() {
        return animals;
    }

    public TickDailyServer getTickables() {
        return ticking;
    }

    public TownTrackerServer getTownTracker() {
        return towns;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        ticking.readFromNBT(nbt.getCompoundTag("TickingDaily"));
        towns.readFromNBT(nbt.getCompoundTag("TownTracker"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("TickingDaily", ticking.writeToNBT(new NBTTagCompound()));
        nbt.setTag("TownTracker", towns.writeToNBT(new NBTTagCompound()));
        return nbt;
    }
}