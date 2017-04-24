package joshie.harvest.core.handlers;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.entity.monster.IMob;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@HFEvents
@SuppressWarnings("unused")
public class MobHandler {
    public static boolean register() { return HFCore.MOBS_ONLY_SPAWN_UNDERGROUND_IN_OVERWORLD < 256; }

    @SubscribeEvent
    public void onEntitySpawned(WorldEvent.PotentialSpawns event) {
        if (event.getWorld().provider.getDimension() == 0 && event.getPos().getY() > HFCore.MOBS_ONLY_SPAWN_UNDERGROUND_IN_OVERWORLD) {
            event.getList().removeIf(e -> IMob.class.isAssignableFrom(e.entityClass));
        }
    }
}
