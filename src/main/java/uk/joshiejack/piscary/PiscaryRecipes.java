package uk.joshiejack.piscary;

import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.DatabaseHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.RecipeHelper;
import uk.joshiejack.piscary.block.BlockHatchery;
import uk.joshiejack.piscary.block.BlockMachine;
import uk.joshiejack.piscary.block.BlockTrap;
import uk.joshiejack.piscary.tile.TileRecyler;
import uk.joshiejack.piscary.item.ItemBait;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static uk.joshiejack.piscary.Piscary.MODID;
import static uk.joshiejack.piscary.block.PiscaryBlocks.*;
import static uk.joshiejack.piscary.item.PiscaryItems.*;
import static uk.joshiejack.piscary.item.ItemFish.Fish.*;
import static uk.joshiejack.piscary.item.ItemLoot.Loot.CAN;
import static uk.joshiejack.piscary.item.ItemMeal.Meal.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class PiscaryRecipes {
    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        DatabaseHelper.registerSimpleMachine(event, "recycler", TileRecyler.registry);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeHelper helper = new RecipeHelper(event.getRegistry(), MODID);
        if (PiscaryConfig.addRecipes) {
            helper.plateRecipe(MEAL, FISHSTICKS, "fishRaw", "bread");
            helper.plateRecipe(MEAL, SASHIMI, "fishRaw");
            helper.bowlRecipe(MEAL, FISH_STEW, "fishRaw", "cropCarrot");
        }

        if (PiscaryConfig.convertableFish) {
            GameRegistry.addSmelting(LOOT.getStackFromEnum(CAN), new ItemStack(Items.IRON_NUGGET, 3), 0.4F);
            helper.shapelessRecipe("clownfish", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), FISH.getStackFromEnum(CLOWN));
            helper.shapelessRecipe("salmon", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata()), FISH.getStackFromEnum(SALMON));
            helper.shapelessRecipe("cod", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()), FISH.getStackFromEnum(COD));
            helper.shapelessRecipe("pufferfish", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), FISH.getStackFromEnum(PUFFER));
        }

        if (PiscaryConfig.enableFishingRodRecipe) {
            helper.shapedRecipe("fishing_rod", new ItemStack(FISHING_ROD), "  L", " LS", "L S", 'L', "logWood", 'S', "string");
        }

        if (PiscaryConfig.enableTrapRecipe) {
            helper.shapedRecipe("fish_trap", FISH_TRAP.getStackFromEnum(BlockTrap.Trap.EMPTY), "WSW", "S S", "WSW", 'W', "logWood", 'S', "string");
        }

        if (PiscaryConfig.enableHatcheryRecipe) {
            helper.shapedRecipe("hatchery", HATCHERY.getStackFromEnum(BlockHatchery.Hatchery.FISH), "F F", "F F", "SSS", 'F', "fenceWood", 'S', "slabWood");
        }

        if (PiscaryConfig.enableBaitRecipes) {
            helper.shapelessRecipe("bait", BAIT.getStackFromEnum(ItemBait.Bait.BASIC, 64), Items.BEEF);
            helper.shapelessRecipe("bait_vanilla", BAIT.getStackFromEnum(ItemBait.Bait.VANILLA, 32), Items.ROTTEN_FLESH);
        }

        if (PiscaryConfig.enableRecyclerRecipe) {
            helper.shapedRecipe("recycler", MACHINE.getStackFromEnum(BlockMachine.Machine.RECYCLER), "SWS", "SPS", "SLS", 'S', "stone", 'W', "stickWood", 'P', Blocks.PISTON, 'L', Blocks.LEVER);
        }
    }
}
