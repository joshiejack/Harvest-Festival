package joshie.harvest.tools;

import joshie.harvest.core.base.ItemBaseTool;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.tools.items.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static joshie.harvest.core.helpers.generic.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.generic.ConfigHelper.getInteger;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFLoader
public class HFTools {
    public static final ItemBaseTool HAMMER = new ItemHammer().register("hammer");
    public static final ItemBaseTool AXE = new ItemAxe().register("axe");

    //Farming Tools
    public static final ItemBaseTool HOE = new ItemHoe().register("hoe");
    public static final ItemBaseTool SICKLE = new ItemSickle().register("sickle");
    public static final ItemBaseTool WATERING_CAN = new ItemWateringCan().register("wateringcan");

    //Potion Effects
    public static final Potion FATIGUE = registerPotion("fatigue", 0xD9D900, 0, 0);
    public static final Potion EXHAUSTION = registerPotion("exhaustion", 0xBBBBBB, 1, 0);

    public static void preInit() {}
    private static Potion registerPotion(String name, int color, int x, int y) {
        ResourceLocation location = new ResourceLocation(MODID, name);
        Potion potion = new HFPotion(MODID + ".effect." + name, color, x, y).setRegistryName(location);
        return GameRegistry.register(potion);
    }

    //Configuration
    public static float EXHAUSTION_AMOUNT;
    public static boolean RESTORE_HUNGER_ON_SLEEP;

    public static void configure() {
        EXHAUSTION_AMOUNT = 4F / getInteger("Actions per Half Haunch", 27);
        RESTORE_HUNGER_ON_SLEEP = getBoolean("Restore Hunger on Sleep", true);
    }
}