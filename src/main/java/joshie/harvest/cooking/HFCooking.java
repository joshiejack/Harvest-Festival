package joshie.harvest.cooking;

import static joshie.harvest.cooking.HFIngredients.milk;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.cooking.entity.EntityCookingItem;
import joshie.harvest.cooking.entity.RenderCookingItem;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
        RenderingRegistry.registerEntityRenderingHandler(EntityCookingItem.class, new RenderCookingItem());
    }
}
