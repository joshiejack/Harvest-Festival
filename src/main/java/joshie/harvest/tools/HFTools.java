package joshie.harvest.tools;

import joshie.harvest.core.base.ItemBaseTool;
import joshie.harvest.tools.items.*;

public class HFTools {
    public static final ItemBaseTool HAMMER = new ItemHammer().register("hammer");
    public static final ItemBaseTool AXE = new ItemAxe().register("axe");

    //Farming Tools
    public static final ItemBaseTool HOE = new ItemHoe().register("hoe");
    public static final ItemBaseTool SICKLE = new ItemSickle().register("sickle");
    public static final ItemBaseTool WATERING_CAN = new ItemWateringCan().register("wateringcan");

    public static void preInit() {}
}