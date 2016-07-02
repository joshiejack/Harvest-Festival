package joshie.harvest.items;

import net.minecraft.item.Item;

public class HFItems {

    public static final ItemBaseTool HAMMER = (ItemBaseTool) new ItemHammer().setUnlocalizedName("hammer");
    public static final ItemBaseTool AXE = (ItemBaseTool) new ItemAxe().setUnlocalizedName("axe");
    public static final ItemCheat CHEAT = new ItemCheat().setUnlocalizedName("cheat");

    //Misc
    public static final Item GENERAL = new ItemGeneral().setUnlocalizedName("general");
}