package joshie.harvest.core.achievements;

import joshie.harvest.animals.HFAnimals;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.block.BlockFlower.FlowerType;
import joshie.harvest.core.block.BlockStorage.Storage;
import joshie.harvest.core.util.HFLoader;
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

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.core.lib.HFModInfo.MODNAME;

@HFLoader
public class HFAchievements {
    public static final AchievementPage PAGE = new AchievementPage(MODNAME);
    public static Achievement summon;
    public static Achievement visitor;
    public static Achievement harvest;
    public static Achievement milker;
    public static Achievement egger;
    public static Achievement firstChristmas;
    public static Achievement birthday;
    public static Achievement firstShipping;
    public static Achievement millionaire;
    public static Achievement theMine;
    public static Achievement deathByChick;
    public static Achievement mystril;

    public static void init() {
        AchievementPage.registerAchievementPage(PAGE);
        summon = addAchievement("summon", 0, 0, HFCore.FLOWERS.getStackFromEnum(FlowerType.GODDESS), null);
        visitor = addAchievement("visitor", 0, 2, new ItemStack(Blocks.RED_FLOWER), summon);
        harvest = addAchievement("harvest", 0, 4, HFCrops.TURNIP.getCropStack(1), visitor);
        milker = addAchievement("milker", -1, 5, HFAnimals.MILK.getStack(Size.SMALL), harvest);
        egger = addAchievement("egger", 1, 5, HFAnimals.EGG.getStack(Size.SMALL), harvest);
        firstChristmas = addAchievement("firstChristmas", 2, -3, new ItemStack(Blocks.SAPLING, 1, 1), summon);
        birthday = addAchievement("birthday", 2, -5, new ItemStack(Blocks.CAKE), firstChristmas);
        firstShipping = addAchievement("firstShipping", -2, 2, HFCore.STORAGE.getStackFromEnum(Storage.SHIPPING), visitor);
        millionaire = addAchievement("millionaire", -4, 2, new ItemStack(Items.GOLD_INGOT), firstShipping);
        theMine = addAchievement("theMine", 3, 0, HFMining.ORE.getStackFromEnum(Ore.ROCK), summon);
        deathByChick = addAchievement("deathByChick", 5, 0, HFMining.DARK_SPAWNER.getStackFromEnum(DarkSpawner.CHICK), theMine);
        mystril = addAchievement("mystril", 7, 0, HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL), theMine);
    }

    private static Achievement addAchievement(String name, int column, int row, ItemStack stack, Achievement parent) {
        Achievement achievement = new Achievement(MODID + ".achievement." + name.replace("_", "."), MODID + "." + name, column, row, stack, parent);
        achievement.registerStat();
        PAGE.getAchievements().add(achievement);
        return achievement;
    }
}
