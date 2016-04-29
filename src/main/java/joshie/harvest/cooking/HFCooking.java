package joshie.harvest.cooking;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.entity.EntityCookingItem;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.cooking.HFIngredients.milk;

public class HFCooking {
    public static Fluid cookingOil;
    public static Fluid cookingMilk;

    public static void preInit() {
        EntityRegistry.registerModEntity(EntityCookingItem.class, "FakeItem", 1, HarvestFestival.instance, 80, 3, false);
        HFApi.COOKING.registerRecipeHandler(new MayoRecipeHandler());

        cookingOil = FluidRegistry.getFluid("oil.cooking");
        if (cookingOil == null) {
            cookingOil = new FluidCookingOil("oil.cooking");
            FluidRegistry.registerFluid(cookingOil);
        }

        cookingMilk = FluidRegistry.getFluid("milk");
        if (milk == null) {
            cookingMilk = new FluidCookingMilk("milk");
            FluidRegistry.registerFluid(cookingMilk);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        //RenderingRegistry.registerEntityRenderingHandler(EntityCookingItem.class, new RenderCookingItem()); //TODO
    }
}