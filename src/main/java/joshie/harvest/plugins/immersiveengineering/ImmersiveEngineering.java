package joshie.harvest.plugins.immersiveengineering;

/*@HFLoader(mods = "immersiveengineering", priority = -1)
@SuppressWarnings("unused")
public class ImmersiveEngineering {
    private static Crop HEMP;

    @ItemStackHolder(value = "immersiveengineering:material", meta = 4)
    @SuppressWarnings("WeakerAccess")
    public static final ItemStack hemp = null;

    @ItemStackHolder(value = "immersiveengineering:seed")
    @SuppressWarnings("WeakerAccess")
    public static final ItemStack hemp_seeds = null;

    public static void preInit() {
        HEMP = new Crop(new ResourceLocation(MODID, "hemp")).setValue(1000, 1).setStages(15).setRegrow(1).setSeedColours(0xB57449).setSeasons(SPRING, SUMMER, AUTUMN)
                .setStateHandler(new StateHandlerHemp(Block.REGISTRY.getObject(new ResourceLocation("immersiveengineering", "hemp")))).setSkipRender()
                .setAnimalFoodType(null).setRequiresSickle(15).setGrowthHandler(new HempGrowthHandler()).setBecomesDouble(15);
    }

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        HEMP.setDropHandler(new DropHandlerHemp(hemp.getItem())).setItem(hemp);
        HFApi.crops.registerSeedForBlacklisting(hemp_seeds);
    }

    @SuppressWarnings("unchecked, unused")
    public static void postInit() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class clazz = Class.forName("blusunrize.immersiveengineering.api.crafting.SqueezerRecipe");
        Method method = clazz.getMethod("addRecipe", FluidStack.class, ItemStack.class, Object.class, int.class);
        method.invoke(null, FluidRegistry.getFluidStack("plantoil", 750), new ItemStack(Blocks.WOOL), HFCrops.SEEDS, 6400);
    }
}*/
