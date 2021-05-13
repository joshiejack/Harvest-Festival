package uk.joshiejack.seasons.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class SeasonsSavedData extends WorldSavedData {
    private static final String DATA_NAME = "Season-Data";
    public static SeasonsSavedData instance;
    private final WorldDataServer world = new WorldDataServer();

    public SeasonsSavedData() { super(DATA_NAME);}
    public SeasonsSavedData(String name) {
        super(name);
    }

    public static WorldDataServer getWorldData(World world) {
        instance = (SeasonsSavedData) world.loadData(SeasonsSavedData.class, DATA_NAME);
        if (instance == null) {
            instance = new SeasonsSavedData(DATA_NAME);
            world.setData(DATA_NAME, instance);
            instance.markDirty(); //Save the file
        }

        return instance.world;
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tag) {
        return world.serializeNBT();
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tag) {
        world.deserializeNBT(tag);
    }
}
