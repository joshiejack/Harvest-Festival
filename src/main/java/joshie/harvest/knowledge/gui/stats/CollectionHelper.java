package joshie.harvest.knowledge.gui.stats;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.mining.HFMining;
import net.minecraft.item.ItemStack;

public class CollectionHelper {
    public static boolean isInFishCollection(ItemStack stack) {
        return InventoryHelper.isOreName(stack, "fish");
    }

    public static boolean isInMiningCollection(ItemStack stack) {
        return stack.getItem() == HFMining.MATERIALS || InventoryHelper.startsWith(stack, "ore") || InventoryHelper.startsWith(stack, "gem");
    }

    public static boolean isInCookingCollection(ItemStack stack) {
        for (Recipe recipe: Recipe.REGISTRY) {
            if (stack.isItemEqual(recipe.getStack())) return true;
        }

        return false;
    }
}
