package joshie.harvest.cooking;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidCookingOil extends Fluid {
    public FluidCookingOil(String fluidName) {
        super(fluidName, new ResourceLocation("blocks/" + fluidName + "_still"), new ResourceLocation("blocks/" + fluidName + "_flow"));
    }
}