package joshie.harvest.knowledge.gui.stats;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.core.Ore;
import joshie.harvest.core.util.holders.HolderRegistrySet;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemJunk.Junk;
import joshie.harvest.mining.HFMining;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import static joshie.harvest.api.core.MatchType.PREFIX;

public class CollectionHelper {
    public static final HolderRegistrySet FISH = new HolderRegistrySet();
    public static final HolderRegistrySet ORE = new HolderRegistrySet();
    static {
        FISH.register(Ore.of("fish"));
        for (Junk junk: Junk.values()) {
            if (junk != Junk.BAIT) FISH.register(HFFishing.JUNK.getStackFromEnum(junk));
        }

        ORE.register(HFMining.MATERIALS);
        ORE.register(Ore.of("ore").setType(PREFIX));
        ORE.register(Ore.of("gem").setType(PREFIX));
    }

    public static boolean isInFishCollection(@Nonnull ItemStack stack) {
        return FISH.contains(stack);
    }

    public static boolean isInMiningCollection(@Nonnull ItemStack stack) {
        return ORE.contains(stack);
    }

    public static boolean isInCookingCollection(@Nonnull ItemStack stack) {
        for (Recipe recipe: Recipe.REGISTRY.values()) {
                if (stack.isItemEqual(recipe.getStack())) return true;
        }

        return false;
    }

    public static boolean isInShippingCollection(@Nonnull ItemStack stack) {
        return !isInFishCollection(stack) && !isInMiningCollection(stack) && !isInCookingCollection(stack);
    }
}
