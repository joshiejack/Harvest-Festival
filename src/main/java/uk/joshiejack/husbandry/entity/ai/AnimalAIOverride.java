package uk.joshiejack.husbandry.entity.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static uk.joshiejack.husbandry.Husbandry.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class AnimalAIOverride {
    @SubscribeEvent
    public static void onEntityJoinedWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (!entity.world.isRemote && entity instanceof EntityAgeable) {
            AnimalStats<?> stats = AnimalStats.getStats(entity);
            if (stats != null) {
                EntityAgeable ageable = ((EntityAgeable) entity);
                stats.getType().getAITraits().forEach(trait -> ageable.tasks.addTask(trait.getPriority(), trait.getAI(ageable, stats)));
            }
        }
    }
}
