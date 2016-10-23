package joshie.harvest.animals.tracker;

import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimalTrackerClient extends AnimalTracker {
    @Override
    public void onDeath(AnimalStats stats) {
        HFTrackers.getClientPlayerTracker().getRelationships().clear(EntityHelper.getEntityUUID(stats.getAnimal()));
    }
}