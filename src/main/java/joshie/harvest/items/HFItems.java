package joshie.harvest.items;

import joshie.harvest.core.config.General;
import joshie.harvest.core.util.base.ItemHFFML;
import net.minecraft.item.Item;

public class HFItems {

    public static final ItemBaseTool HAMMER = (ItemBaseTool) new ItemHammer().setUnlocalizedName("hammer");
    public static final ItemBaseTool AXE = (ItemBaseTool) new ItemAxe().setUnlocalizedName("axe");

    //Misc
    public static final Item GENERAL = new ItemGeneral().setUnlocalizedName("general.item");

    public static void preInit() {
        //Add the debug item
        if (General.DEBUG_MODE) {
            new ItemCheat().setUnlocalizedName("cheat");
        }
    }
}