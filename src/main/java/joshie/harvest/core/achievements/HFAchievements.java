package joshie.harvest.core.achievements;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.util.HFLoader;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockOre.Ore;
import joshie.harvest.mining.item.ItemDarkSpawner.DarkSpawner;
import joshie.harvest.mining.item.ItemMaterial.Material;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.item.ItemNPCTool.NPCTool;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.HFModInfo.MODNAME;

@HFLoader
public class HFAchievements {
    public static final AchievementPage PAGE = new AchievementPage(MODNAME);
    public static Achievement summon;
    public static Achievement friend;
    public static Achievement marriage;
    public static Achievement harvest;
    public static Achievement onion;
    public static Achievement spinach;
    public static Achievement milker;
    public static Achievement egger;
    public static Achievement milkerLarge;
    public static Achievement eggerLarge;
    public static Achievement firstChristmas;
    public static Achievement birthday;
    public static Achievement firstShipping;
    public static Achievement millionaire;
    public static Achievement theMine;
    public static Achievement killChick;
    public static Achievement killChicken;
    public static Achievement killSheep;
    public static Achievement killCow;
    public static Achievement junk;
    public static Achievement copper;
    public static Achievement silver;
    public static Achievement gold;
    public static Achievement mystril;
    public static Achievement cooking;
    public static Achievement recipes;

    public static void init() {
        AchievementPage.registerAchievementPage(PAGE);
        summon = addAchievement("summon", 0, 0, HFCore.FLOWERS.getStackFromEnum(FlowerType.GODDESS), null);
        friend = addAchievement("friend", 2, 3, new ItemStack(Items.COOKIE), summon);
        marriage = addAchievement("marriage", 4, 3, HFNPCs.TOOLS.getStackFromEnum(NPCTool.BLUE_FEATHER), summon);
        harvest = addAchievement("harvest", 0, 4, HFCrops.TURNIP.getCropStack(1), summon);
        onion = addAchievement("onion", 0, 7, HFCrops.ONION.getCropStack(1), harvest);
        spinach = addAchievement("spinach", 0, 9, HFCrops.SPINACH.getCropStack(1), harvest);
        milker = addAchievement("milker", -2, 6, HFAnimals.MILK.getStack(Size.SMALL), harvest);
        egger = addAchievement("egger", 2, 6, HFAnimals.EGG.getStack(Size.SMALL), harvest);
        milkerLarge = addAchievement("milkerLarge", -4, 6, HFAnimals.MILK.getStack(Size.LARGE), milker);
        eggerLarge = addAchievement("eggerLarge", 4, 6, HFAnimals.EGG.getStack(Size.LARGE), egger);
        firstChristmas = addAchievement("firstChristmas", 2, -3, new ItemStack(Blocks.SAPLING, 1, 1), summon);
        birthday = addAchievement("birthday", 2, -5, new ItemStack(Items.CAKE), firstChristmas);
        firstShipping = addAchievement("firstShipping", -2, 3, HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING), summon);
        millionaire = addAchievement("millionaire", -4, 3, new ItemStack(Items.GOLD_INGOT), firstShipping);
        theMine = addAchievement("theMine", 3, 0, HFMining.ORE.getStackFromEnum(Ore.ROCK), summon);
        killChick = addAchievement("killChick", 5, -1, HFMining.DARK_SPAWNER.getStackFromEnum(DarkSpawner.CHICK), theMine);
        killChicken = addAchievement("killChicken", 7, -1, HFMining.DARK_SPAWNER.getStackFromEnum(DarkSpawner.CHICKEN), killChick);
        killSheep = addAchievement("killSheep", 9, -1, HFMining.DARK_SPAWNER.getStackFromEnum(DarkSpawner.SHEEP), killChicken);
        killCow = addAchievement("killCow", 11, -1, HFMining.DARK_SPAWNER.getStackFromEnum(DarkSpawner.COW), killSheep);
        junk = addAchievement("junk", 5, 1, HFMining.MATERIALS.getStackFromEnum(Material.JUNK), theMine);
        copper = addAchievement("copper", 7, 1, HFMining.MATERIALS.getStackFromEnum(Material.COPPER), junk);
        silver = addAchievement("silver", 9, 1, HFMining.MATERIALS.getStackFromEnum(Material.SILVER), copper);
        gold = addAchievement("gold", 11, 1, HFMining.MATERIALS.getStackFromEnum(Material.GOLD), silver);
        mystril = addAchievement("mystril", 13, 1, HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL), gold);
        cooking = addAchievement("cooking", -2, 0, HFApi.cooking.getBestMeal("turnip_pickled"), summon);
        recipes = addAchievement("recipes", -4, 0, CookingHelper.getRecipe("turnip_pickled"), summon);
    }

    private static Achievement addAchievement(String name, int column, int row, ItemStack stack, Achievement parent) {
        Achievement achievement = new Achievement(MODID + ".achievement." + name.replace("_", "."), MODID + "." + name, column, row, stack, parent);
        achievement.registerStat();
        PAGE.getAchievements().add(achievement);
        return achievement;
    }

    /** Resetting the itemstack because it may have changed **/
    public static void remap() throws Exception {
        setFinalField(harvest, HFCrops.TURNIP.getCropStack(1));
        setFinalField(onion, HFCrops.ONION.getCropStack(1));
        setFinalField(spinach, HFCrops.SPINACH.getCropStack(1));
        setFinalField(cooking, HFApi.cooking.getBestMeal("turnip_pickled"));
    }

    private static void setFinalField(Achievement achievement, ItemStack stack) throws Exception {
        Field field = ReflectionHelper.findField(Achievement.class, "theItemStack", "field_75990_d");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(achievement, stack);
    }
}
