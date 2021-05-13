package uk.joshiejack.settlements.world.gen;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.harvestcore.event.GatherTownCentres;
import uk.joshiejack.harvestcore.event.WildernessCheckSpawn;
import uk.joshiejack.harvestcore.HCConfig;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class WildernessSpawner {
    @SubscribeEvent
    public static void onGatherLocations(GatherTownCentres event) {
        Arrays.stream(TownFinder.towns(event.getWorld()))
                .forEach(t -> event.add(t.getCentre()));
    }

    @SubscribeEvent
    public static void onSpawnCheck(WildernessCheckSpawn event) {
        Town<?> town = TownFinder.find(event.getWorld(), event.getTarget());
        if (town.getLandRegistry().getClosestBuilding(event.getWorld(),
                event.getTarget()).getCentre().getDistance(event.getTarget().getX(),
                event.getTarget().getY(), event.getTarget().getZ()) <= HCConfig.minWildernessDistance) {
            event.setResult(Event.Result.DENY);
        }
    }
}

