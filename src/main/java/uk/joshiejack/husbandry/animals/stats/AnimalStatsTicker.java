package uk.joshiejack.husbandry.animals.stats;

import com.google.common.collect.Sets;
import uk.joshiejack.husbandry.HusbandryConfig;
import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.CapabilityHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.WorldHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;

import static uk.joshiejack.husbandry.Husbandry.MODID;
import static uk.joshiejack.husbandry.animals.stats.CapabilityStatsHandler.ANIMAL_STATS_CAPABILITY;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class AnimalStatsTicker {
    public static final Set<AnimalStats<?>> stats = Sets.newHashSet();

    @SubscribeEvent
    public static void onNewDay(NewDayEvent event) {
        stats.forEach(AnimalStats::onNewDay);
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (HusbandryConfig.happySun && WorldHelper.isBihourly(event)) {
            stats.forEach(AnimalStats::onBihourlyTick);
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (!event.getEntity().world.isRemote) {
            AnimalStats<?> stats = CapabilityHelper.getCapabilityFromEntity(event.getEntity(), ANIMAL_STATS_CAPABILITY);
            if (stats != null) {
                AnimalStatsTicker.stats.remove(stats);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote) {
            AnimalStats<?> stats = CapabilityHelper.getCapabilityFromEntity(event.getEntity(), ANIMAL_STATS_CAPABILITY);
            if (stats != null) {
                AnimalStatsTicker.stats.add(stats);
            }
        }
    }

    @SubscribeEvent //Remove all the stats when the chunk is unloaded
    public static void onChunkUnloaded(ChunkEvent.Unload event) {
        if (!event.getWorld().isRemote) {
            for (ClassInheritanceMultiMap<Entity> map : event.getChunk().getEntityLists()) {
                map.forEach((entity) -> {
                    AnimalStats<?> stats = CapabilityHelper.getCapabilityFromEntity(entity, ANIMAL_STATS_CAPABILITY);
                    if (stats != null) {
                        AnimalStatsTicker.stats.add(stats);
                    }
                });
            }
        }
    }
}
