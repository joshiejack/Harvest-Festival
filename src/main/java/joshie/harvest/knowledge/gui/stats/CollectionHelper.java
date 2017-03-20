package joshie.harvest.knowledge.gui.stats;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.core.Ore;
import joshie.harvest.core.util.holders.HolderRegistrySet;
import joshie.harvest.mining.HFMining;
import net.minecraft.item.ItemStack;

import static joshie.harvest.api.core.MatchType.PREFIX;

public class CollectionHelper {
    public static final HolderRegistrySet FISH = new HolderRegistrySet();
    public static final HolderRegistrySet ORE = new HolderRegistrySet();
    static {
        FISH.register(Ore.of("fish"));
        ORE.register(HFMining.MATERIALS);
        ORE.register(Ore.of("ore").setType(PREFIX));
        ORE.register(Ore.of("gem").setType(PREFIX));
    }

    public static boolean isInFishCollection(ItemStack stack) {
        return FISH.contains(stack);
    }

    public static boolean isInMiningCollection(ItemStack stack) {
        return ORE.contains(stack);
    }

    public static boolean isInCookingCollection(ItemStack stack) {
        for (Recipe recipe: Recipe.REGISTRY.values()) {
                if (stack.isItemEqual(recipe.getStack())) return true;
        }

        return false;
    }

    public static boolean isInShippingCollection(ItemStack stack) {
        return !isInFishCollection(stack) && !isInMiningCollection(stack) && !isInCookingCollection(stack);
    }
}
