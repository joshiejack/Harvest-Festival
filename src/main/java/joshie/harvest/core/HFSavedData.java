package joshie.harvest.core;

import joshie.harvest.animals.tracker.AnimalTrackerServer;
import joshie.harvest.core.handlers.DailyTickHandler;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.town.tracker.TownTrackerServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import javax.annotation.Nonnull;

public class HFSavedData extends WorldSavedData {
    public static final String DATA_NAME = HFModInfo.CAPNAME + "-Data";
    private final AnimalTrackerServer animals = new AnimalTrackerServer();
    private final DailyTickHandler ticking = new DailyTickHandler();
    private final TownTrackerServer towns = new TownTrackerServer();
    //TODO: Remove in 0.7+
    private NBTTagCompound temp;

    public HFSavedData(String string) {
        super(string);
    }

    public void setWorld(World world) {
        animals.setWorld(world);
        ticking.setWorld(world);
        towns.setWorld(world);
        //TODO: Remove in 0.7+
        if (temp != null && world.provider.getDimension() == 0) {
            HFTrackers.<TownTrackerServer>getTowns(world).readFromNBT(temp);
            HFTrackers.markTownsDirty();
            temp = null; //Reset the world
        }
    }

    public AnimalTrackerServer getAnimalTracker() {
        return animals;
    }

    public DailyTickHandler getTickables() {
        return ticking;
    }

    public TownTrackerServer getTownTracker() {
        return towns;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        if (nbt.hasKey("TownTracker")) {
            temp = nbt.getCompoundTag("TownTracker");
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        return nbt;
    }
}