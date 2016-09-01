package joshie.harvest.shops.purchaseable;

import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.recipe.MealImpl;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class PurchaseableMeal extends PurchaseableFML<MealImpl> {
    public PurchaseableMeal(long cost, String meal) {
        super(cost, meal);
    }

    @Override
    public IForgeRegistry<MealImpl> getRegistry() {
        return CookingAPI.REGISTRY;
    }

    @Override
    public ItemStack getDisplayStack() {
        return item.cook(item.getMeal());
    }
}
