package uk.joshiejack.energy.events;

import uk.joshiejack.energy.Energy;
import uk.joshiejack.energy.EnergyConfig;
import uk.joshiejack.energy.EnergyEffects;
import uk.joshiejack.energy.EnergyFoodStats;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Energy.MODID)
public class SleepEvents {
    @SubscribeEvent
    public static void onSleepTimeCheck(SleepingTimeCheckEvent event) {
        if (EnergyConfig.sleepAnytime)
            event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public static void onWakeup(PlayerWakeUpEvent event) {
        event.getEntityPlayer().getEntityData().setLong("LastSlept", event.getEntityPlayer().world.getWorldTime()); //Update the last sleeping timer
        EnergyUsageEvents.updatePotionEffects(event.getEntityPlayer());
        if (EnergyConfig.sleepRestoresEnergy) {
            World world = event.getEntityPlayer().world;
            if (!world.isRemote && world.getWorldTime() % 24000 == 0) {
                EnergyFoodStats stats = (EnergyFoodStats) event.getEntityPlayer().getFoodStats();
                boolean fatigued = event.getEntityPlayer().isPotionActive(EnergyEffects.FATIGUE);
                boolean exhausted = event.getEntityPlayer().isPotionActive(EnergyEffects.EXHAUSTION);
                int restored = (int) (stats.maxFoodDisplay * (fatigued ? 0.75 : exhausted ? 0.5 : 1));
                event.getEntityPlayer().getFoodStats().addStats(restored, restored * 5);
            }
        }
    }
}
