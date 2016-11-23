package joshie.harvest.tools;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.base.item.ItemToolChargeable;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.tools.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.ConfigHelper.getInteger;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.entity.SharedMonsterAttributes.*;

@HFLoader
public class HFTools {
    public static final ItemHammer HAMMER = new ItemHammer().register("hammer");
    public static final ItemAxe AXE = new ItemAxe().register("axe");

    //Farming Tools
    public static final ItemTool SICKLE = new ItemSickle().register("sickle");
    public static final ItemToolChargeable HOE = new ItemHoe().register("hoe");
    public static final ItemWateringCan WATERING_CAN = new ItemWateringCan().register("wateringcan");

    //Potion Effects
    public static final Potion FATIGUE = registerPotion("fatigue", 0xD9D900, 0, 0).registerPotionAttributeModifier(MOVEMENT_SPEED, "8107BC5E-7CF8-4030-440C-514C1F160890", -0.10000000596046448D, 2);
    public static final Potion EXHAUSTION = registerPotion("exhaustion", 0xBBBBBB, 1, 0)
            .registerPotionAttributeModifier(MOVEMENT_SPEED, "8107BC5D-5CF8-4030-440C-314C1E160890", -0.50000000596046448D, 2)
            .registerPotionAttributeModifier(ATTACK_SPEED, "8107BC5D-5CF8-4030-440C-314C1E160891", -0.50000000596046448D, 2);
    public static final Potion CURSED = registerPotion("cursed", 0x660000, 2, 0)
            .registerPotionAttributeModifier(MAX_HEALTH, "FB353E1C-4180-4865-B01B-BCCE9785ACA3", -0.33D, 2)
            .registerPotionAttributeModifier(MOVEMENT_SPEED, "8107BD5E-7CF8-4030-441C-514C1F160890", -0.03000000596046448D, 2)
            .registerPotionAttributeModifier(ATTACK_DAMAGE, "8107BD5F-4CF8-4030-441D-534C1F140890", -0.20000000596046448D, 2);

    public static void preInit() {
        registerSounds("smash_rock", "smash_wood");
    }

    public static void init() {
        HFApi.npc.getGifts().addToBlacklist(HAMMER, AXE, SICKLE, WATERING_CAN, HOE);
    }

    private static Potion registerPotion(String name, int color, int x, int y) {
        ResourceLocation location = new ResourceLocation(MODID, name);
        Potion potion = new HFPotion(MODID + ".effect." + name, color, x, y).setRegistryName(location);
        return GameRegistry.register(potion);
    }

    //Configuration
    public static float EXHAUSTION_AMOUNT;
    public static boolean RESTORE_HUNGER_ON_SLEEP;
    public static boolean RESTORE_HUNGER_ON_FAINTING;
    public static boolean HF_CONSUME_HUNGER;
    public static boolean ATTACK_FAINTING;
    public static boolean BLOCK_FAINTING;

    public static void configure() {
        EXHAUSTION_AMOUNT = 4F / getInteger("Actions per half haunch", 27);
        RESTORE_HUNGER_ON_SLEEP = getBoolean("Restore hunger on sleep", true);
        RESTORE_HUNGER_ON_SLEEP = getBoolean("Restore hunger on fainting", true);
        HF_CONSUME_HUNGER = getBoolean("Performing Harvest Festival actions consumes hunger", true);
        ATTACK_FAINTING = getBoolean("Attack entities has chance of fainting", false);
        BLOCK_FAINTING = getBoolean("Breaking blocks has chance of fainting", false);
    }
}