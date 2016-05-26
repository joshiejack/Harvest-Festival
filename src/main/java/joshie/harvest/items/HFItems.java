package joshie.harvest.items;

import joshie.harvest.core.config.General;
import net.minecraft.item.Item;

public class HFItems {

    public static final ItemBaseTool HAMMER = (ItemBaseTool) new ItemHammer().setUnlocalizedName("hammer");
    public static final ItemBaseTool AXE = (ItemBaseTool) new ItemAxe().setUnlocalizedName("axe");

    //Misc
    public static final Item GENERAL = new ItemGeneral().setUnlocalizedName("general.item");
    public static final ItemMeal MEAL = (ItemMeal) new ItemMeal().setUnlocalizedName("meal");

    //Misc
    public static final ItemBuilding STRUCTURES = (ItemBuilding) new ItemBuilding().setUnlocalizedName("structures");
    public static final Item SPAWNER_NPC = new ItemNPCSpawner().setUnlocalizedName("spawner.npc");

    public static void preInit() {
        //Add the debug item
        if (General.DEBUG_MODE) {
            new ItemCheat().setUnlocalizedName("cheat");
        }
    }
}