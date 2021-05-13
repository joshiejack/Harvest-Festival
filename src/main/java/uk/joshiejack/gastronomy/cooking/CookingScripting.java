package uk.joshiejack.gastronomy.cooking;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.api.GastronomyAPI;
import uk.joshiejack.gastronomy.api.ItemCookedEvent;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingMethods;
import uk.joshiejack.penguinlib.scripting.event.ScriptingTriggerFired;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Gastronomy.MODID)
public class CookingScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("cooking", CookingScripting.class);
    }

    @SubscribeEvent
    public static void onCollectScriptingMethods(CollectScriptingMethods event) {
        event.add("onItemCooked");
    }

    @SubscribeEvent
    public static void onCooked(ItemCookedEvent event) {
        if (event.getEntityPlayer().world.isRemote) return;
            MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onItemCooked", event.getEntityPlayer(), event.getAppliance().getName(), event.getStack()));
    }

    public static boolean hasRecipe(ItemStackJS wrapper) {
        return GastronomyAPI.instance.hasRecipe(wrapper.penguinScriptingObject);
    }
}
