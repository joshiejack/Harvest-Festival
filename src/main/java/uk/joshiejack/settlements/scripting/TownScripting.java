package uk.joshiejack.settlements.scripting;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import uk.joshiejack.settlements.scripting.wrappers.AbstractTownJS;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.scripting.WrapperRegistry;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PositionJS;
import uk.joshiejack.penguinlib.scripting.wrappers.WorldServerJS;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.UUID;

import static uk.joshiejack.settlements.Settlements.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class TownScripting {
    private static final Cache<UUID, Integer> townsFromTeamToTownID = CacheBuilder.newBuilder().build();

    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("towns", TownScripting.class);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static AbstractTownJS get(PlayerJS player) {
        //Find the town that belongs to this player
        UUID team = player.team().penguinScriptingObject.getID();
        for (Town<?> town : TownFinder.towns(player.penguinScriptingObject.world)) {
            UUID owner = town.getCharter().getTeamID();
            if (owner.equals(team)) {
                return WrapperRegistry.wrap(town);
            }
        }

        return null;
    }

    public static AbstractTownJS find(WorldServerJS world, PositionJS position) {
        return WrapperRegistry.wrap(TownFinder.find(world.penguinScriptingObject, position.penguinScriptingObject));
    }
}
