package uk.joshiejack.penguinlib;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
@Config(modid = MOD_ID)
public class PenguinConfig {
    @Config.Comment("Add recipes for dinnerware items")
    public static boolean addDishRecipes = true;
    @Config.Comment("Force enable dinnerware item")
    public static boolean forceDinnerwareItem = false;
    @Config.Comment("Food requires dishes in their recipe")
    public static boolean requireDishes = true;
    @Config.Comment("When eating the food, if dish requirement is on, will return the empty dishes to the player")
    public static boolean returnDishes = true;
    @Config.Comment("Enable automation of machines")
    public static boolean enableAutomation = false; //Default to false, allow people to enable it if they wish
    @Config.Comment("Enable debugging tools")
    public static boolean enableDebuggingTools = false;
}