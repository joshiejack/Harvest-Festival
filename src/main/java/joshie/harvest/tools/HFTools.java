package joshie.harvest.tools;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.tools.item.*;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumMap;
import java.util.Locale;

import static joshie.harvest.core.helpers.ConfigHelper.getBoolean;
import static joshie.harvest.core.helpers.ConfigHelper.getInteger;
import static joshie.harvest.core.helpers.RegistryHelper.registerSounds;
import static joshie.harvest.core.lib.HFModInfo.MODID;
import static net.minecraft.entity.SharedMonsterAttributes.*;

@HFLoader
public class HFTools {
    public static final EnumMap<ToolTier, ItemHammer> HAMMERS = new EnumMap<>(ToolTier.class);
    public static final EnumMap<ToolTier, ItemAxe> AXES = new EnumMap<>(ToolTier.class);
    public static final EnumMap<ToolTier, ItemSickle> SICKLES = new EnumMap<>(ToolTier.class);
    public static final EnumMap<ToolTier, ItemHoe> HOES = new EnumMap<>(ToolTier.class);
    public static final EnumMap<ToolTier, ItemWateringCan> WATERING_CANS = new EnumMap<>(ToolTier.class);
    static {
        for (ToolTier tier: ToolTier.values()) {
            HAMMERS.put(tier, new ItemHammer(tier).register("hammer_" + tier.name().toLowerCase(Locale.ENGLISH)));
            AXES.put(tier, new ItemAxe(tier).register("axe_" + tier.name().toLowerCase(Locale.ENGLISH)));
            SICKLES.put(tier, new ItemSickle(tier).register("sickle_" + tier.name().toLowerCase(Locale.ENGLISH)));
            HOES.put(tier, new ItemHoe(tier).register("hoe_" + tier.name().toLowerCase(Locale.ENGLISH)));
            WATERING_CANS.put(tier, new ItemWateringCan(tier).register("watering_can_" + tier.name().toLowerCase(Locale.ENGLISH)));
        }
    }

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
        registerSounds("smash_rock", "smash_wood", "tree_chop", "tree_fall");
    }

    public static void init() {
        for (ToolTier tier: ToolTier.values()) {
            HFApi.npc.getGifts().addToBlacklist(HAMMERS.get(tier), AXES.get(tier), SICKLES.get(tier), WATERING_CANS.get(tier), HOES.get(tier));
        }

        for (ToolTier tier: ToolTier.values()) {
            HFApi.crops.registerSickle(new ItemStack(SICKLES.get(tier), 1, OreDictionary.WILDCARD_VALUE));
        }
    }

    private static Potion registerPotion(String name, int color, int x, int y) {
        ResourceLocation location = new ResourceLocation(MODID, name);
        Potion potion = new HFPotion(MODID + ".effect." + name, color, x, y).setRegistryName(location);
        return GameRegistry.register(potion);
    }

    //Configuration
    static float EXHAUSTION_AMOUNT;
    static boolean RESTORE_HUNGER_ON_SLEEP;
    static boolean RESTORE_HUNGER_ON_FAINTING;
    static boolean HF_CONSUME_HUNGER;
    static boolean ENABLE_FAINTING;
    static boolean ENABLE_EARLY_FAINTING;
    static boolean ENABLE_DEATH_FAINTING;
    static boolean ENABLE_FAINTING_SLEEP;
    static boolean ATTACK_FAINTING;
    static boolean BLOCK_FAINTING;

    public static void configure() {
        EXHAUSTION_AMOUNT = 4F / getInteger("Actions per half haunch", 27);
        RESTORE_HUNGER_ON_SLEEP = getBoolean("Restore hunger on sleep", true);
        RESTORE_HUNGER_ON_SLEEP = getBoolean("Restore hunger on fainting", true);
        HF_CONSUME_HUNGER = getBoolean("Performing Harvest Festival actions consumes hunger", true);
        ENABLE_FAINTING = getBoolean("Enable fainting when low on food", true);
        ENABLE_EARLY_FAINTING = getBoolean("Enable fainting when exhausted and timer is three quarters way", false);
        ENABLE_DEATH_FAINTING = getBoolean("Kill the player instead of fainting", false);
        ENABLE_FAINTING_SLEEP = getBoolean("Force the next day when a player faints and is sent to bed if possible", true);
        ATTACK_FAINTING = getBoolean("Attack entities has chance of fainting", false);
        BLOCK_FAINTING = getBoolean("Breaking blocks has chance of fainting", false);
    }
}