package joshie.harvest.shops.purchasable;

import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.recipe.MealImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class PurchasableMeal extends PurchasableFML<MealImpl> {
    public PurchasableMeal(long cost, ResourceLocation resource) {
        super(cost, resource);
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
