package joshie.harvest.core.handlers;

import joshie.harvest.animals.AnimalTrackerClient;
import joshie.harvest.town.TownTrackerClient;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler extends SideHandler {
    private final AnimalTrackerClient animals = new AnimalTrackerClient();
    private final TownTrackerClient town = new TownTrackerClient();

    public ClientHandler() {}

    @Override
    public void setWorld(World world) {
        animals.setWorld(world);
        town.setWorld(world);
    }

    @Override
    public AnimalTrackerClient getAnimalTracker() {
        return animals;
    }

    @Override
    public TownTrackerClient getTownTracker() {
        return town;
    }

    public boolean getSex() {
        return true;
    }
}
