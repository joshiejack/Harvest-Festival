package uk.joshiejack.harvestcore.scripting;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.database.Collections;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class CollectionScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("collections", CollectionScripting.class);
    }

    public static boolean isIn(String name, ItemStackJS item) {
        return Collections.ALL.containsKey(name) && Collections.ALL.get(name).isInCollection(item.penguinScriptingObject);
    }

    public static boolean completed(PlayerJS playerWrapper, String name) {
        Collections.Collection collection = Collections.ALL.get(name);
        if (collection == null) {
            HarvestCore.logger.log(Level.ERROR, "No such collection known as " + name + " could be found");
            return false;
        } else return collection.getList().stream().allMatch(list -> collection.isObtained(playerWrapper.penguinScriptingObject, list.get(0)));
    }
}
