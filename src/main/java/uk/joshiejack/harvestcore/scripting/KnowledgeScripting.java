package uk.joshiejack.harvestcore.scripting;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.registry.Blueprint;
import uk.joshiejack.harvestcore.registry.Note;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class KnowledgeScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("knowledge", KnowledgeScripting.class);
    }

    public static void unlockBlueprint(PlayerJS player, String name) {
        Blueprint blueprint = Blueprint.REGISTRY.get(new ResourceLocation(name));
        if (blueprint != null && !player.world().penguinScriptingObject.isRemote) {
            blueprint.unlock(player.penguinScriptingObject);
        }
    }

    public static void unlockNote(PlayerJS player, String name) {
        Note note = Note.REGISTRY.get(new ResourceLocation(name));
        if (note != null && !player.world().penguinScriptingObject.isRemote) {
            note.unlock(player.penguinScriptingObject);
        }
    }
}
