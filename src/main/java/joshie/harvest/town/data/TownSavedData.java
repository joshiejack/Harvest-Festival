package joshie.harvest.town.data;

import joshie.harvest.town.tracker.TownTrackerServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

import javax.annotation.Nonnull;

public class TownSavedData extends WorldSavedData {
    private final TownTrackerServer server = new TownTrackerServer();

    public TownSavedData(String string) {
        super(string);
    }

    public TownTrackerServer getData() {
        return server;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        server.readFromNBT(nbt);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        return server.writeToNBT(nbt);
    }
}
