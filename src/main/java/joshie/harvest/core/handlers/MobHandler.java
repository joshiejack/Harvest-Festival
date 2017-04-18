package joshie.harvest.core.handlers;

import joshie.harvest.core.HFCore;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.entity.monster.IMob;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@HFEvents
@SuppressWarnings("unused")
public class MobHandler {
    public static boolean register() { return HFCore.MOBS_ONLY_SPAWN_UNDERGROUND_IN_OVERWORLD < 256; }

    @SubscribeEvent
    public void onEntitySpawned(CheckSpawn event) {
        if (event.getWorld().provider.getDimension() == 0 && event.getY() > HFCore.MOBS_ONLY_SPAWN_UNDERGROUND_IN_OVERWORLD) {
            if (event.getEntityLiving() instanceof IMob) {
                event.setResult(Result.DENY);
            }
        }
    }
}
