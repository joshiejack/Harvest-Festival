package joshie.harvest.tools;

import joshie.harvest.core.util.base.ItemBaseTool;
import joshie.harvest.tools.items.*;
import joshie.harvest.tools.render.RenderToolLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HFTools {
    public static final ItemBaseTool HAMMER = new ItemHammer().register("hammer");
    public static final ItemBaseTool AXE = new ItemAxe().register("axe");

    //Farming Tools
    public static final ItemBaseTool HOE = new ItemHoe().register("hoe");
    public static final ItemBaseTool SICKLE = new ItemSickle().register("sickle");
    public static final ItemBaseTool WATERING_CAN = new ItemWateringCan().register("wateringcan");

    public static void preInit() {}
    @SideOnly(Side.CLIENT)
    public static void preInitClient() {
        MinecraftForge.EVENT_BUS.register(new RenderToolLevel());
    }
}