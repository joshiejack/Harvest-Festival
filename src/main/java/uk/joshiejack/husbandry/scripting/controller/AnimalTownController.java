package uk.joshiejack.husbandry.scripting.controller;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.settlements.scripting.wrappers.AbstractTownJS;
import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldJS;

import javax.annotation.Nonnull;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Husbandry.MODID)
public class AnimalTownController {
    public static int getTown(EntityAgeable animal) {
        AnimalFindTownEvent event = new AnimalFindTownEvent(animal);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() != Event.Result.DENY) {
            return 0;
        } else return event.getTownID();
    }

    public static class AnimalFindTownEvent extends EntityEvent {
        private int townID = 0;

        public AnimalFindTownEvent(Entity entity) {
            super(entity);
        }

        public void setTownID(int townID) {
            this.townID = townID;
        }

        public int getTownID() {
            return townID;
        }
    }

    @Mod.EventBusSubscriber(modid = Husbandry.MODID)
    public static class Scripting {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
            event.registerVar("animalCount", Scripting.class);
        }

        public static int get(@Nonnull WorldJS<?> world, @Nonnull AbstractTownJS<?> townJS, String entity) {
            int townID = townJS.penguinScriptingObject.getID();
            return (int) world.penguinScriptingObject
                    .getEntities(Objects.requireNonNull(EntityList.getClass(new ResourceLocation(entity))), EntitySelectors.IS_ALIVE)
                    .stream().filter(e -> {
                        AnimalStats<?> stats = AnimalStats.getStats(e);
                        return stats != null && stats.getTown() == townID;
                    }).count();
        }
    }
}
