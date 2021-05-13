package uk.joshiejack.energy;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;

@Mod.EventBusSubscriber(modid = Energy.MODID)
@Config(modid = Energy.MODID)
public class EnergyConfig {
    @Config.Comment("Modify vanilla food stats")
    public static boolean modifyVanillaFoods = true;
    @Config.Comment("Make soups stackable")
    public static boolean stackableSoups = true;
    @Config.Comment("Force natural regen to be disabled")
    public static boolean forceNaturalRegenDisabling = false;
    public static boolean sleepRestoresEnergy = true;
    public static boolean sleepAnytime = true;
    public static int startingEnergy = 6;
    public static int startingHealth = 6;

    @SubscribeEvent
    public static void onConfigLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, Energy.MODID, EnergyConfig.class);
    }
}
