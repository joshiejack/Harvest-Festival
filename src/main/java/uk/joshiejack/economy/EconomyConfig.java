package uk.joshiejack.economy;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;

@Config(modid = Economy.MODID) //TODO: Loadconfig, forge can't handle long values, But do we need it?
@Mod.EventBusSubscriber(modid = Economy.MODID)
public class EconomyConfig {
    public static int maxGold = Integer.MAX_VALUE;
    public static int maxGoldMultiplier = Short.MAX_VALUE;
    public static int minGold = 0;
    public static boolean enableHUD = true;
    public static boolean enableGoldIcon = true;
    public static boolean goldLeft = true;
    public static int goldX = 0;
    public static int goldY = 0;
    public static boolean enableDebug = true;

    @SubscribeEvent
    public static void onConfigLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, Economy.MODID, EconomyConfig.class);
    }
}
