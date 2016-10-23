package joshie.harvest.plugins.immersiveengineering;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.core.util.annotations.HFLoader;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.core.lib.HFModInfo.MODID;


@HFLoader(mods = "immersiveengineering", priority = -1)
public class ImmersiveEngineering {
    private static StateHandlerHemp STATE_HANDLER;
    public static Crop HEMP;

    @ItemStackHolder(value = "immersiveengineering:material", meta = 4)
    public static final ItemStack hemp = null;

    @ItemStackHolder(value = "immersiveengineering:seed")
    public static final ItemStack hemp_seeds = null;

    public static void preInit() {
        STATE_HANDLER = new StateHandlerHemp(Block.REGISTRY.getObject(new ResourceLocation("immersiveengineering", "hemp")));
        HEMP = new Crop(new ResourceLocation(MODID, "hemp")).setGoldValues(1000, 1).setStages(15).setRegrowStage(1).setSeedColours(0xB57449).setSeasons(SPRING, SUMMER, AUTUMN).setStateHandler(STATE_HANDLER).setSkipRender()
                .setAnimalFoodType(null).setRequiresSickle(15).setGrowthHandler(new HempGrowthHandler()).setBecomesDouble(15);
    }

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        HEMP.setDropHandler(new DropHandlerHemp(hemp.getItem())).setItem(hemp);
        HFApi.crops.registerSeedForBlacklisting(hemp_seeds);
    }

    @SuppressWarnings("unchecked")
    public static void postInit() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class clazz = Class.forName("blusunrize.immersiveengineering.api.crafting.SqueezerRecipe");
        Method method = clazz.getMethod("addRecipe", FluidStack.class, ItemStack.class, Object.class, int.class);
        method.invoke(null, FluidRegistry.getFluidStack("plantoil", 750), new ItemStack(Blocks.WOOL), HFCrops.SEEDS, 6400);
    }
}
