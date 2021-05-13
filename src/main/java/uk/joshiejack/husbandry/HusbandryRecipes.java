package uk.joshiejack.husbandry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.husbandry.block.*;
import uk.joshiejack.husbandry.item.*;
import uk.joshiejack.penguinlib.util.helpers.minecraft.RecipeHelper;

import static uk.joshiejack.husbandry.Husbandry.MODID;
import static uk.joshiejack.husbandry.block.HusbandryBlocks.*;
import static uk.joshiejack.husbandry.item.HusbandryItems.*;


@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class HusbandryRecipes {
    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeHelper helper = new RecipeHelper(event.getRegistry(), MODID);
        if (HusbandryConfig.enableToolRecipes) {
            helper.shapedRecipe("brush", TOOL.getStackFromEnum(ItemTool.Tool.BRUSH), " W", "S ", 'W', "cropWheat", 'S', "stickWood");
            helper.shapedRecipe("milker", TOOL.getStackFromEnum(ItemTool.Tool.MILKER), "H", "S", "N", 'H', Blocks.HOPPER, 'S', "stickWood", 'N', "nuggetIron");
            helper.shapedRecipe("love_potion", TOOL.getStackFromEnum(ItemTool.Tool.MIRACLE_POTION), " A ", "CBW", " F ", 'A', "cropApple", 'C', "cropCarrot", 'B', Items.GLASS_BOTTLE, 'W', "cropWheat", 'F', "fish");
            helper.shapedRecipe("sickle", new ItemStack(SICKLE), "SS", " W", "W ", 'S', "stone", 'W', "stickWood");
        }

        if (HusbandryConfig.enableFeedRecipes) {
            ResourceLocation bird_feed = new ResourceLocation(MODID, "bird_feed");
            helper.shapelessRecipe("bird_feed_wheat", bird_feed, FEED.getStackFromEnum(ItemFeed.Feed.BIRD_FEED), Items.WHEAT_SEEDS);
            helper.shapelessRecipe("bird_feed_melon", bird_feed, FEED.getStackFromEnum(ItemFeed.Feed.BIRD_FEED), Items.MELON_SEEDS);
            helper.shapelessRecipe("bird_feed_pumpkin", bird_feed, FEED.getStackFromEnum(ItemFeed.Feed.BIRD_FEED), Items.PUMPKIN_SEEDS);
            helper.shapelessRecipe("bird_feed_beetroot", bird_feed, FEED.getStackFromEnum(ItemFeed.Feed.BIRD_FEED), Items.BEETROOT_SEEDS);
            helper.shapelessRecipe("cat_food", FEED.getStackFromEnum(ItemFeed.Feed.CAT_FOOD, 2), "fishCod", "fishSalmon");
            helper.shapelessRecipe("dog_food", FEED.getStackFromEnum(ItemFeed.Feed.DOG_FOOD, 2), Items.CHICKEN, Items.BEEF);
            helper.shapelessRecipe("rabbit_food", FEED.getStackFromEnum(ItemFeed.Feed.RABBIT_FOOD), "cropCarrot");
            helper.shapelessRecipe("slop", FEED.getStackFromEnum(ItemFeed.Feed.SLOP, 3), Items.BREAD, "cropMelon", "cropWheat");
        }

        if (HusbandryConfig.enableFoodRecipes) {
            FurnaceRecipes.instance().addSmeltingRecipe(DRINK.getStackFromEnum(ItemDrink.Drink.SMALL_MILK), DRINK.getStackFromEnum(ItemDrink.Drink.HOT_MILK, 1), 1F);
            FurnaceRecipes.instance().addSmeltingRecipe(DRINK.getStackFromEnum(ItemDrink.Drink.MEDIUM_MILK), DRINK.getStackFromEnum(ItemDrink.Drink.HOT_MILK, 2), 1F);
            FurnaceRecipes.instance().addSmeltingRecipe(DRINK.getStackFromEnum(ItemDrink.Drink.LARGE_MILK), DRINK.getStackFromEnum(ItemDrink.Drink.HOT_MILK, 3), 1F);
            helper.shapelessRecipe("dinnerroll", FOOD.getStackFromEnum(ItemFood.Food.DINNERROLL), Items.BREAD, "foodButter", "egg");
            FurnaceRecipes.instance().addSmeltingRecipe(FOOD.getStackFromEnum(ItemFood.Food.SMALL_EGG), FOOD.getStackFromEnum(ItemFood.Food.BOILED_EGG, 1), 1F);
            FurnaceRecipes.instance().addSmeltingRecipe(FOOD.getStackFromEnum(ItemFood.Food.MEDIUM_EGG), FOOD.getStackFromEnum(ItemFood.Food.BOILED_EGG, 2), 1F);
            FurnaceRecipes.instance().addSmeltingRecipe(FOOD.getStackFromEnum(ItemFood.Food.LARGE_EGG), FOOD.getStackFromEnum(ItemFood.Food.BOILED_EGG, 3), 1F);
            FurnaceRecipes.instance().addSmeltingRecipe(FOOD.getStackFromEnum(ItemFood.Food.SMALL_DUCK_EGG), FOOD.getStackFromEnum(ItemFood.Food.BOILED_EGG, 1), 1F);
            FurnaceRecipes.instance().addSmeltingRecipe(FOOD.getStackFromEnum(ItemFood.Food.MEDIUM_DUCK_EGG), FOOD.getStackFromEnum(ItemFood.Food.BOILED_EGG, 2), 1F);
            FurnaceRecipes.instance().addSmeltingRecipe(FOOD.getStackFromEnum(ItemFood.Food.LARGE_DUCK_EGG), FOOD.getStackFromEnum(ItemFood.Food.BOILED_EGG, 3), 1F);
            FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(Items.EGG), FOOD.getStackFromEnum(ItemFood.Food.BOILED_EGG, 2), 1F);

            //Various recipes for butter
            ResourceLocation butter = new ResourceLocation(MODID, "butter");
            helper.shapelessRecipe("butter_small", butter, FOOD.getStackFromEnum(ItemFood.Food.BUTTER, 1), DRINK.getStackFromEnum(ItemDrink.Drink.SMALL_MILK));
            helper.shapelessRecipe("butter_medium", butter, FOOD.getStackFromEnum(ItemFood.Food.BUTTER, 2), DRINK.getStackFromEnum(ItemDrink.Drink.MEDIUM_MILK));
            helper.shapelessRecipe("butter_large", butter, FOOD.getStackFromEnum(ItemFood.Food.BUTTER, 3), DRINK.getStackFromEnum(ItemDrink.Drink.LARGE_MILK));

            //Various ice cream recipes....
            ResourceLocation ice_cream = new ResourceLocation(MODID, "ice_cream");
            helper.shapelessRecipe("ice_cream_small", ice_cream, FOOD.getStackFromEnum(ItemFood.Food.ICE_CREAM, 1), Items.BOWL, DRINK.getStackFromEnum(ItemDrink.Drink.SMALL_MILK), "egg");
            helper.shapelessRecipe("ice_cream_medium", ice_cream, FOOD.getStackFromEnum(ItemFood.Food.ICE_CREAM, 2), Items.BOWL, DRINK.getStackFromEnum(ItemDrink.Drink.MEDIUM_MILK), "egg");
            helper.shapelessRecipe("ice_cream_large", ice_cream, FOOD.getStackFromEnum(ItemFood.Food.ICE_CREAM, 3), Items.BOWL, DRINK.getStackFromEnum(ItemDrink.Drink.LARGE_MILK), "egg");
        }

        if (HusbandryConfig.enableMachineRecipes) {
            helper.shapedRecipe("spinning_wheel", MACHINE.getStackFromEnum(BlockMachine.Machine.SPINNING_WHEEL), " S ", "SWS", "PPP", 'S', "stickWood", 'W', "string", 'P', "plankWood");
            helper.shapedRecipe("oil_maker", MACHINE.getStackFromEnum(BlockMachine.Machine.OIL_MAKER), "SGS", "SBS", "SPS", 'S', "stone", 'G', "blockGlass", 'B', Items.GLASS_BOTTLE, 'P', Blocks.PISTON);
            helper.shapedRecipe("bee_hive", MACHINE.getStackFromEnum(BlockMachine.Machine.HIVE), "SSS", "L L", "LLL", 'S', "slabWood", 'L', "logWood");
            helper.shapedRecipe("incubator", INCUBATOR.getStackFromEnum(BlockIncubator.Fill.EMPTY), "IHI", "PPP", 'I', "ingotIron", 'H', Blocks.HAY_BLOCK, 'P', "plankWood");
            helper.shapedRecipe("fermenter", DOUBLE_MACHINE.getStackFromEnum(BlockDoubleMachine.Machine.FERMENTER), "PPP", "G G", "LML", 'P', "slabWood", 'G', "blockGlass", 'M', Blocks.PISTON, 'L', "logWood");
        }

        if (HusbandryConfig.enableGenericTreatRecipe) {
            helper.shapelessRecipe("treat", TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), " N ", "NSN", " N ", 'N', "nuggetGold", 'S', Items.SUGAR);
        }

        if (HusbandryConfig.enableTreatRecipes) {
            helper.shapelessRecipe("cat_treat", TREAT.getStackFromEnum(ItemTreat.Treat.CAT), TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), Items.FISH);
            helper.shapelessRecipe("horse_treat", TREAT.getStackFromEnum(ItemTreat.Treat.HORSE), TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), Items.APPLE);
            helper.shapelessRecipe("cow_treat", TREAT.getStackFromEnum(ItemTreat.Treat.COW), TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), Items.POTATO);
            helper.shapelessRecipe("sheep_treat", TREAT.getStackFromEnum(ItemTreat.Treat.SHEEP), TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), Items.WHEAT);
            helper.shapelessRecipe("chicken_treat", TREAT.getStackFromEnum(ItemTreat.Treat.CHICKEN), TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), Items.PUMPKIN_SEEDS);
            helper.shapelessRecipe("rabbit_treat", TREAT.getStackFromEnum(ItemTreat.Treat.RABBIT), TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), Items.CARROT);
            helper.shapelessRecipe("pig_treat", TREAT.getStackFromEnum(ItemTreat.Treat.PIG), TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), Blocks.PUMPKIN);
            helper.shapelessRecipe("dog_treat", TREAT.getStackFromEnum(ItemTreat.Treat.DOG), TREAT.getStackFromEnum(ItemTreat.Treat.GENERIC), Items.BONE);
        }

        if (HusbandryConfig.enableFeederRecipes) {
            helper.shapedRecipe("feeder_tray", TRAY.getStackFromEnum(BlockTray.Tray.FEEDER_EMPTY), "BSB", 'B', "plankWood", 'S', "slabWood");
            helper.shapedRecipe("feeder_trough", TROUGH.getStackFromEnum(BlockTrough.Section.SINGLE), "P P", "PPP", "L L", 'P', "plankWood", 'L', "logWood");
            helper.shapedRecipe("feeder_bowl", TRAY.getStackFromEnum(BlockTray.Tray.BOWL_EMPTY), " S ", "S S", " S ", 'S', "slabWood");
        }

        if (HusbandryConfig.enableNestRecipe) {
            helper.shapedRecipe("nest", TRAY.getStackFromEnum(BlockTray.Tray.NEST_EMPTY), "PHP", "PPP", 'H', Blocks.HAY_BLOCK, 'P', "plankWood");
        }
    }
}
