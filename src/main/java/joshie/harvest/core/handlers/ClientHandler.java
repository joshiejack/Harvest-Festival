package joshie.harvest.core.handlers;

import joshie.harvest.animals.tracker.AnimalTrackerClient;
import joshie.harvest.core.util.handlers.SideHandler;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientHandler extends SideHandler {
    private final AnimalTrackerClient animals = new AnimalTrackerClient();

    public ClientHandler() {}

    @Override
    public void setWorld(World world) {
        animals.setWorld(world);
    }

    @Override
    public AnimalTrackerClient getAnimalTracker() {
        return animals;
    }
}
