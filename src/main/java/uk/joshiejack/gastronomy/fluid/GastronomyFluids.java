package uk.joshiejack.gastronomy.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.penguinlib.util.helpers.forge.RegistryHelper;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

@SuppressWarnings("unused")
public class GastronomyFluids {
    public static final Fluid COOKING_OIL = RegistryHelper.createFluid("cooking_oil", new ResourceLocation(MODID, "blocks/fluids/cooking_oil"), Gastronomy.TAB);
    public static final Fluid MILK = RegistryHelper.createFluid("milk", new ResourceLocation(MODID, "blocks/fluids/milk"), Gastronomy.TAB);
    public static final Fluid WINE = RegistryHelper.createFluid("wine", new ResourceLocation(MODID, "blocks/fluids/wine"), Gastronomy.TAB);

    public static void load() {}
}
