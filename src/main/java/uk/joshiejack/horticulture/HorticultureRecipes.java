package uk.joshiejack.horticulture;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.horticulture.block.BlockMachine;
import uk.joshiejack.horticulture.block.BlockSprinkler;
import uk.joshiejack.horticulture.tileentity.TileSeedMaker;
import uk.joshiejack.horticulture.tileentity.TileStump;
import uk.joshiejack.penguinlib.data.adapters.StateAdapter;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.DatabaseHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.RecipeHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import java.util.Set;

import static uk.joshiejack.horticulture.Horticulture.MODID;
import static uk.joshiejack.horticulture.block.HorticultureBlocks.*;
import static uk.joshiejack.horticulture.item.HorticultureItems.*;
import static uk.joshiejack.horticulture.item.ItemDrink.Drink.*;
import static uk.joshiejack.horticulture.item.ItemFood.Meal.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class HorticultureRecipes {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("stump").rows().forEach(row -> {
            String item = row.get("item");
            ItemStack stack = StackHelper.getStackFromString(item);
            IBlockState state = StateAdapter.fromString(row.get("state"));
            TileStump.registry.register(Holder.getFromStack(stack), new TileStump.MushroomData(stack, state, row.get("limit"), row.getColor("color")));
            StackHelper.registerSynonym(item + "_spores", StackHelper.withNBT(SPORES, stack.writeToNBT(new NBTTagCompound())));
        });

        DatabaseHelper.registerSimpleMachine(event, "seed_maker", TileSeedMaker.registry);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("ConstantConditions")
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        if (HorticultureConfig.addRecipes) {
            RecipeHelper helper = new RecipeHelper(event.getRegistry(), MODID);
            helper.bowlRecipe(MEAL, CORNFLAKES, "cropCorn", Items.MILK_BUCKET);
            helper.glassRecipe(DRINK, PINEAPPLE_JUICE, "cropPineapple");
            helper.glassRecipe(DRINK, TOMATO_JUICE, "cropTomato");
            helper.glassRecipe(DRINK, STRAWBERRY_MILK, "cropStrawberry", Items.MILK_BUCKET);
            helper.glassRecipe(DRINK, GRAPE_JUICE, "cropGrapes");
            helper.glassRecipe(DRINK, PEACH_JUICE, "cropPeach");
            helper.glassRecipe(DRINK, BANANA_JUICE, "cropBanana");
            helper.glassRecipe(DRINK, ORANGE_JUICE, "cropOrange");
            helper.glassRecipe(DRINK, APPLE_JUICE, Items.APPLE);

            helper.picklingJarRecipe(MEAL, PICKLED_CUCUMBER, "cropCucumber");

            helper.bowlRecipe(MEAL, CORNFLAKES, "cropCorn", Items.MILK_BUCKET);
            helper.bowlRecipe(MEAL, CANDIED_POTATO, "cropSweetPotato");
            helper.bowlRecipe(MEAL, BOILED_SPINACH, "cropSpinach");
            helper.bowlRecipe(MEAL, STIR_FRY, "cropCabbage");
            helper.bowlRecipe(MEAL, PUMPKIN_STEW, Blocks.PUMPKIN);
            helper.bowlRecipe(MEAL, SALAD, "cropOnion", "cropGreenPepper", "cropTomato");

            helper.plateRecipe(MEAL, BAKED_CORN, "cropCorn");
            helper.plateRecipe(MEAL, HAPPY_EGGPLANT, "cropEggplant");
            helper.plateRecipe(MEAL, PICKLED_TURNIP, "cropTurnip");

            if (HorticultureConfig.recipeWateringCan) {
                helper.shapedRecipe("watering_can", new ItemStack(WATERING_CAN), "S  ", "SBS", " S ", 'S', "stone", 'B', Items.BUCKET);
            }

            if (HorticultureConfig.enableSeedMakerRecipe) {
                helper.shapedRecipe("seed_maker", MACHINE.getStackFromEnum(BlockMachine.Machine.SEED_MAKER), "WCW", "WRW", "WFW", 'C', CROP, 'W', "plankWood", 'R', Items.REDSTONE, 'F', Blocks.FURNACE);
            }


            if (HorticultureConfig.enableSprinklerRecipe) {
                ResourceLocation sprinklers = new ResourceLocation(MODID, "sprinkler");
                helper.shapedRecipe("sprinkler_old", sprinklers, SPRINKLER.getStackFromEnum(BlockSprinkler.Sprinkler.OLD), " L ", "LBL", " L ", 'L', "plankWood", 'B', new ItemStack(Items.DYE, 1, 15));
                helper.shapedRecipe("sprinkler_iron", sprinklers, SPRINKLER.getStackFromEnum(BlockSprinkler.Sprinkler.IRON), " L ", "LBL", " L ", 'L', "ingotIron", 'B', new ItemStack(Items.DYE, 1, 15));
            }

            if (HorticultureConfig.enableMushroomLogRecipe) {
                ResourceLocation mushroom_logs = new ResourceLocation(MODID, "mushroom_log");
                Set<Block> mushrooms = Sets.newHashSet(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM);
                for (Block mushroom: mushrooms) {
                    helper.shapedRecipe("mushroom_log_oak_" + mushroom.getRegistryName().getPath(), mushroom_logs, STUMP.getStackFromEnum(BlockPlanks.EnumType.OAK), "M", "L", 'L', new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.getMetadata()), 'M', mushroom);
                    helper.shapedRecipe("mushroom_log_spruce_" + mushroom.getRegistryName().getPath(), mushroom_logs, STUMP.getStackFromEnum(BlockPlanks.EnumType.SPRUCE), "M", "L", 'L', new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()), 'M', mushroom);
                    helper.shapedRecipe("mushroom_log_birch_" + mushroom.getRegistryName().getPath(), mushroom_logs, STUMP.getStackFromEnum(BlockPlanks.EnumType.BIRCH), "M", "L", 'L', new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.BIRCH.getMetadata()), 'M', mushroom);
                    helper.shapedRecipe("mushroom_log_jungle_" + mushroom.getRegistryName().getPath(), mushroom_logs, STUMP.getStackFromEnum(BlockPlanks.EnumType.JUNGLE), "M", "L", 'L', new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()), 'M', mushroom);
                    helper.shapedRecipe("mushroom_log_acacia_" + mushroom.getRegistryName().getPath(), mushroom_logs, STUMP.getStackFromEnum(BlockPlanks.EnumType.ACACIA), "M", "L", 'L', new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.ACACIA.getMetadata()), 'M', mushroom);
                    helper.shapedRecipe("mushroom_log_dark_oak_" + mushroom.getRegistryName().getPath(), mushroom_logs, STUMP.getStackFromEnum(BlockPlanks.EnumType.DARK_OAK), "M", "L", 'L', new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()), 'M', mushroom);
                }
            }
        }
    }
}
