package joshie.harvest.knowledge.stats.collection.page;

import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.item.ItemFish.Fish;
import net.minecraft.item.ItemStack;

import static joshie.harvest.knowledge.stats.CollectionHelper.isInFishCollection;

public class PageFishing extends PageShipping {
    public static final BookPage INSTANCE = new PageFishing();

    private PageFishing() {
        super("fishing", HFFishing.FISH.getStackFromEnum(Fish.PUPFISH));
    }

    @Override
    public ItemStack getIcon() {
        return HFFishing.FISH.getStackFromEnum(Fish.PUPFISH);
    }

    @Override
    boolean qualifies(ItemStack stack) {
        return isInFishCollection(stack);
    }
}
