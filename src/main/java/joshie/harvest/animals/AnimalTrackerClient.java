package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimalTrackerClient extends AnimalTracker {
    @Override
    public void onDeath(IAnimalTracked animal) {
        HFTrackers.getClientPlayerTracker().getRelationships().clear(animal.getUUID());
    }
}