package joshie.harvest.fishing;

import joshie.harvest.api.fishing.FishingManager;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.holders.HolderRegistry;
import joshie.harvest.core.util.holders.HolderRegistrySet;
import joshie.harvest.knowledge.gui.stats.CollectionHelper;
import net.minecraft.item.ItemStack;

@HFApiImplementation
public class FishingAPI implements FishingManager {
    public static final FishingAPI INSTANCE = new FishingAPI();
    private final HolderRegistrySet baits = new HolderRegistrySet();
    final HolderRegistry<Integer> breeding = new HolderRegistry<>();

    @Override
    public void registerForFishingCollection(Object object) {
        CollectionHelper.FISH.register(object);
    }

    @Override
    public void registerBait(ItemStack stack) {
        baits.register(stack);
    }

    @Override
    public void registerAsBreedable(ItemStack stack, int days) {
        breeding.register(stack, days);
    }

    public boolean isBait(ItemStack stack) {
        return baits.contains(stack);
    }

    public int getDaysFor(ItemStack stack) {
        Integer days = breeding.getValueOf(stack);
        return days == null ? -1 : days;
    }
}
