package uk.joshiejack.energy.scripting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.energy.Energy;
import uk.joshiejack.energy.EnergyFoodStats;
import uk.joshiejack.energy.events.AddExhaustionEvent;
import uk.joshiejack.energy.scripting.wrappers.AddExhaustionEventJS;
import uk.joshiejack.penguinlib.events.CollectScriptingWrappers;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingMethods;
import uk.joshiejack.penguinlib.scripting.event.ScriptingTriggerFired;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;

@Mod.EventBusSubscriber(modid = Energy.MODID)
public class EnergyScripting {
    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("energy", EnergyScripting.class);
    }

    @SubscribeEvent
    public static void onCollectWrappers(CollectScriptingWrappers event) {
        event.register(AddExhaustionEventJS.class, AddExhaustionEvent.class);
    }

    @SubscribeEvent
    public static void onCollectScriptingMethods(CollectScriptingMethods event) {
        event.add("onExhaustionAdded");
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onExhaustionAdded(AddExhaustionEvent event) {
        MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onExhaustionAdded", event));
    }

    public static boolean increaseMaxEnergy(PlayerJS playerWrapper) {
        EntityPlayer player = playerWrapper.penguinScriptingObject;
        return player.getFoodStats() instanceof EnergyFoodStats && ((EnergyFoodStats)player.getFoodStats()).increaseMaxEnergy();
    }

    public static boolean increaseMaxHealth(PlayerJS playerWrapper) {
        EntityPlayer player = playerWrapper.penguinScriptingObject;
        return player.getFoodStats() instanceof EnergyFoodStats && ((EnergyFoodStats)player.getFoodStats()).increaseMaxHealth();
    }
}
