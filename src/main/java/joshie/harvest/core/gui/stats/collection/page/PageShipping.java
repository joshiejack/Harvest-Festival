package joshie.harvest.core.gui.stats.collection.page;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.gui.stats.GuiStats;
import joshie.harvest.core.gui.stats.collection.button.ButtonShipped;
import joshie.harvest.core.registry.ShippingRegistry;
import joshie.harvest.core.util.holders.AbstractItemHolder;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.item.ItemCrop.Crops;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import java.util.List;

import static joshie.harvest.core.gui.stats.CollectionHelper.isInFishCollection;
import static joshie.harvest.core.gui.stats.CollectionHelper.isInMiningCollection;

public class PageShipping extends PageCollection {
    public static final BookPage INSTANCE = new PageShipping();

    private PageShipping() {
        super("shipping", HFCrops.CROP.getStackFromEnum(Crops.STRAWBERRY));
    }

    PageShipping(String string, ItemStack stack) {
        super(string, stack);
    }

    boolean qualifies(ItemStack stack) {
        return !isInFishCollection(stack) && !isInMiningCollection(stack);
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList) {
        super.initGui(gui, buttonList); //Add the tabs
        List<AbstractItemHolder> list = ShippingRegistry.INSTANCE.getRegistry().getStacks();
        int j = 0;
        int k = 0;
        int l = 0;
        int added = 0;
        for (int i = 0; added <= 144 && i < list.size(); i++) {
            AbstractItemHolder holder = list.get(i);
            if (!qualifies(holder.getMatchingStacks().get(0))) continue;
            long value = ShippingRegistry.INSTANCE.getRegistry().getValue(holder);
            boolean obtained = hasObtainedStack(holder);
            if (k == 7) {
                k = 0;
                j++;
            }

            k++;
            if (j == 8) {
                j = 0;
                k = 1;
                l += 142;
            }

            added++;
            buttonList.add(new ButtonShipped(gui, holder, value, obtained, buttonList.size(), l + 3 + k * 18, 24 + j * 18));
        }
    }

    private boolean hasObtainedStack(AbstractItemHolder holder) {
        for (ItemStack stack: holder.getMatchingStacks()) {
            if (HFTrackers.getClientPlayerTracker().getTracking().hasObtainedItem(stack)) return true;
        }

        return false;
    }
}
