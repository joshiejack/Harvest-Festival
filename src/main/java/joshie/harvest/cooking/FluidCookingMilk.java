package joshie.harvest.cooking;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidCookingMilk extends Fluid {
    public FluidCookingMilk(String name) {
        super(name, new ResourceLocation("blocks/" + name + "_still"), new ResourceLocation("blocks/" + name + "_flow"));
    }
}