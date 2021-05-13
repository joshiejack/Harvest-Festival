package uk.joshiejack.settlements;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.forge.ConfigHelper;

import static uk.joshiejack.settlements.Settlements.MODID;

@Config(modid = MODID)
public class AdventureConfig {
    @Config.Comment("Enable quest tracking book")
    public static boolean questTrackingBook = true;
    @Config.Comment("Distance between buildings that counts as same town")
    public static int townDistance = 128;
    @Config.Comment("Enable automatic respawning of npcs on the next day")
    public static boolean autoInviteNPCOnDeath = false;
    @Config.Comment("Enable missing gift value logging")
    public static String enableGiftLoggingForModIDs = "minecraft,harvestfestival,gastronomy,horticulture,husbandry,piscary";
    @Config.Comment("Enable missing gift value logging")
    public static boolean enableGiftLogging = false;
    @Config.Comment("Prevent buildings from demolished without the demo tool")
    public static boolean enableBuildingProtection = true;

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.ConfigLoad event) throws IllegalAccessException {
        ConfigHelper.readFromDatabase(event, Settlements.MODID, AdventureConfig.class);
    }
}
