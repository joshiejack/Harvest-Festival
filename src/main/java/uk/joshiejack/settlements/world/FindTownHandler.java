package uk.joshiejack.settlements.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.husbandry.scripting.controller.AnimalTownController;

@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class FindTownHandler {
    @SubscribeEvent
    public static void onAnimalFindTown(AnimalTownController.AnimalFindTownEvent event) {
        Entity animal = event.getEntity();
        event.setTownID(TownFinder.find(animal.world, new BlockPos(animal)).getID());
        event.setResult(Event.Result.DENY);
    }
}
