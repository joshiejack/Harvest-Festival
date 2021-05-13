package uk.joshiejack.harvestcore.world.gen;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.harvestcore.event.GatherTownCentres;
import uk.joshiejack.harvestcore.event.WildernessCheckSpawn;

public class VanillaVillageWildernessSpawner {
    @SubscribeEvent
    public static void onGatherLocations(GatherTownCentres event) {
        event.getWorld().getVillageCollection().getVillageList()
                .forEach(village -> event.add(village.getCenter()));
    }

    @SubscribeEvent
    public static void onSpawnCheck(WildernessCheckSpawn event) {
        if (event.getWorld().getVillageCollection().getVillageList()
                .stream().anyMatch(v -> v.isBlockPosWithinSqVillageRadius(event.getTarget()))) {
            event.setResult(Event.Result.DENY);
        }
    }
}
