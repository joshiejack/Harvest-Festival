package uk.joshiejack.harvestcore.scripting;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingFunctions;
import uk.joshiejack.penguinlib.scripting.wrappers.ItemStackJS;
import uk.joshiejack.penguinlib.scripting.wrappers.PlayerJS;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.util.Strings;

@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class AchievementScripting {
    public static final HolderRegistry<ResourceLocation> ACHIEVEMENTS = new HolderRegistry<>(new ResourceLocation("minecraft:air"));

    @SubscribeEvent
    public static void onCollectScriptingFunctions(CollectScriptingFunctions event) {
        event.registerVar("achievements", AchievementScripting.class);
    }

    public static boolean isNullOrEmpty(ResourceLocation resource) {
        return resource == null || resource.toString().equals("minecraft:air");
    }

    public static boolean isInCollection(ItemStackJS item) {
        return !isNullOrEmpty(ACHIEVEMENTS.getValue(item.penguinScriptingObject));
    }

    public static int getValue(PlayerJS player, ItemStackJS item) {
        ResourceLocation resource = ACHIEVEMENTS.getValue(item.penguinScriptingObject);
        Interpreter script = isNullOrEmpty(resource) ? null : Scripting.get(resource);
        return script == null || !script.hasMethod("getValue") ? 0 : (int) script.getResultOfFunction("getValue", player.penguinScriptingObject, item.penguinScriptingObject);
    }

    public static String formatValue(PlayerJS player, ItemStackJS wrapper, int value) {
        ItemStack item = wrapper.penguinScriptingObject;
        ResourceLocation resource = ACHIEVEMENTS.getValue(item);
        Interpreter script = isNullOrEmpty(resource) ? null : Scripting.get(resource);
        return script == null || !script.hasMethod("formatValue") ? Strings.EMPTY : (String) script.getResultOfFunction("formatValue", player.penguinScriptingObject, item, value);
    }

    public static boolean isObtained(PlayerJS player, ItemStackJS wrapper) {
        ItemStack item = wrapper.penguinScriptingObject;
        ResourceLocation resource = ACHIEVEMENTS.getValue(item);
        Interpreter script = isNullOrEmpty(resource) ? null : Scripting.get(resource);
        return script != null && script.isTrue("isObtained", player.penguinScriptingObject, item);
    }
}
