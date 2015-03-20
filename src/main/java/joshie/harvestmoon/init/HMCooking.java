package joshie.harvestmoon.init;

import joshie.harvestmoon.init.cooking.HMFryingPanRecipes;
import joshie.harvestmoon.init.cooking.HMIngredients;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;


public class HMCooking {
    public static Fluid cookingOil;
    public static Fluid milk;
    
    public static void init() {
        cookingOil = new Fluid("oil.cooking");
        FluidRegistry.registerFluid(cookingOil);
        
        milk = FluidRegistry.getFluid("milk");
        if (milk == null) {
            milk = new Fluid("milk");
            FluidRegistry.registerFluid(milk);
        }
        
        HMIngredients.init();
        HMFryingPanRecipes.init();
    }
}
