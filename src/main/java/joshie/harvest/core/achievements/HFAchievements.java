package joshie.harvest.core.achievements;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.animals.item.ItemAnimalProduct.Sizeable;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Size;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockOre.Ore;
import joshie.harvest.mining.item.ItemDarkSpawner.DarkSpawner;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import javax.annotation.Nonnull;

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
    public static Achievement cabbage;
    public static Achievement pineapple;
    public static Achievement greenPepper;
    public static Achievement strawberries;
    public static Achievement corn;
    public static Achievement sweetPotatoes;
    public static Achievement cucumbers;
    public static Achievement tomatoes;
    public static Achievement eggplants;

    @SuppressWarnings("unused")
    public static void postInit() {
        AchievementPage.registerAchievementPage(PAGE);
        summon = addAchievement("summon", 0, 0, HFCore.FLOWERS.getStackFromEnum(FlowerType.GODDESS), null);
        friend = addAchievement("friend", 2, 2, new ItemStack(Items.COOKIE), summon);
        //marriage = addAchievement("marriage", 4, 3, HFNPCs.TOOLS.getStackFromEnum(NPCTool.BLUE_FEATHER), summon); //TODO: Readd when i do marriage
        harvest = addAchievement("harvest", 0, 4, HFCrops.TURNIP.getCropStack(1), summon);
        onion = addAchievement("onion", 0, 6, HFCrops.ONION.getCropStack(1), harvest);
        spinach = addAchievement("spinach", 0, 8, HFCrops.SPINACH.getCropStack(1), onion);
        cabbage = addAchievement("cabbage", 2, 4, HFCrops.CABBAGE.getCropStack(1), harvest);
        pineapple = addAchievement("pineapple", 2, 6, HFCrops.PINEAPPLE.getCropStack(1), onion);
        sweetPotatoes = addAchievement("sweetPotatoes", 2, 8, HFCrops.SWEET_POTATO.getCropStack(1), spinach);
        cucumbers = addAchievement("cucumber", -2, 4, HFCrops.CUCUMBER.getCropStack(1), harvest);
        tomatoes = addAchievement("tomato", -2, 6, HFCrops.TOMATO.getCropStack(1), onion);
        eggplants = addAchievement("eggplant", -2, 8, HFCrops.EGGPLANT.getCropStack(1), spinach);
        strawberries = addAchievement("strawberries", -4, 4, HFCrops.STRAWBERRY.getCropStack(1), cucumbers);
        corn = addAchievement("corn", -4, 6, HFCrops.CORN.getCropStack(1), tomatoes);
        greenPepper = addAchievement("greenPepper", -4, 8, HFCrops.GREEN_PEPPER.getCropStack(1), eggplants);

        milker = addAchievement("milker", 2, -2, HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.SMALL), summon);
        egger = addAchievement("egger", -2, -2, HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.SMALL), summon);
        milkerLarge = addAchievement("milkerLarge", 2, -4, HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.MILK, Size.LARGE), milker);
        eggerLarge = addAchievement("eggerLarge", -2, -4, HFAnimals.ANIMAL_PRODUCT.getStack(Sizeable.EGG, Size.LARGE), egger);
        firstChristmas = addAchievement("firstChristmas", 0, -3, new ItemStack(Blocks.SAPLING, 1, 1), summon);
        birthday = addAchievement("birthday", 0, -5, new ItemStack(Items.CAKE), firstChristmas);
        firstShipping = addAchievement("firstShipping", -2, 2, HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING), summon);
        millionaire = addAchievement("millionaire", -4, 2, new ItemStack(Items.GOLD_INGOT), firstShipping);
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

    private static Achievement addAchievement(String name, int column, int row, @Nonnull ItemStack stack, Achievement parent) {
        Achievement achievement = new Achievement(MODID + ".achievement." + name.replace("_", "."), MODID + "." + name, column, row, stack, parent);
        achievement.registerStat();
        PAGE.getAchievements().add(achievement);
        return achievement;
    }
}
