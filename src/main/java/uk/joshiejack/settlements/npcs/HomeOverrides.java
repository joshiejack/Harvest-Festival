package uk.joshiejack.settlements.npcs;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

import static uk.joshiejack.settlements.Settlements.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class HomeOverrides {
    private static final Map<ResourceLocation, String> waypoints = Maps.newHashMap();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {//LOW PRIORITY, TODO: Move to "AdventureNPCs"
        event.table("npc_home_overrides").rows()
                .forEach(override -> waypoints.put(new ResourceLocation(override.get("npc_id")), override.get("home")));
    }

    public static String get(ResourceLocation npc) {
        return waypoints.containsKey(npc) ? waypoints.get(npc) : npc.toString();
    }
}
