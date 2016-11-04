package joshie.harvest.shops.purchasable;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.CookingHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;

public class PurchasableMeal extends PurchasableFML<Recipe> {
    private ItemStack stack;

    public PurchasableMeal(long cost, ResourceLocation resource) {
        super(cost, resource);
    }

    @Override
    public IForgeRegistry<Recipe> getRegistry() {
        return Recipe.REGISTRY;
    }

    @Override
    public ItemStack getDisplayStack() {
        if (stack == null) {
            stack = CookingHelper.makeRecipe(item);
            stack.getTagCompound().setLong(SELL_VALUE, 0L);
        }

        return stack;
    }
}
