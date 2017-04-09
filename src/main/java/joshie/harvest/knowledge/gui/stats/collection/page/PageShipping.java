package joshie.harvest.knowledge.gui.stats.collection.page;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import joshie.harvest.HarvestFestival;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.registry.ShippingRegistry;
import joshie.harvest.core.util.holders.AbstractItemHolder;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.item.ItemCrop.Crops;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonNext;
import joshie.harvest.knowledge.gui.stats.button.ButtonPrevious;
import joshie.harvest.knowledge.gui.stats.collection.button.ButtonSearch;
import joshie.harvest.knowledge.gui.stats.collection.button.ButtonShipped;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static joshie.harvest.knowledge.gui.stats.CollectionHelper.isInShippingCollection;

public class PageShipping extends PageCollection {
    public static final BookPage INSTANCE = new PageShipping();
    private final Cache<String, List<AbstractItemHolder>> cache = CacheBuilder.newBuilder().maximumSize(64).build();
    private static final List<AbstractItemHolder> EMPTY = Lists.newArrayList();

    private PageShipping() {
        super("shipping", HFCrops.CROP.getStackFromEnum(Crops.STRAWBERRY));
    }

    PageShipping(String string, ItemStack stack) {
        super(string, stack);
    }

    boolean qualifies(ItemStack stack) {
        return isInShippingCollection(stack);
    }

    private List<AbstractItemHolder> getList() {
        try {
            return cache.get(search, () -> {
                List<AbstractItemHolder> list = new ArrayList<>();
                for (AbstractItemHolder holder : ShippingRegistry.INSTANCE.getRegistry().getStacks()) {
                    List<ItemStack> stacks = holder.getMatchingStacks();
                    if (stacks.size() > 0 && qualifies(stacks.get(0))
                            && (Strings.isNullOrEmpty(search) || (hasObtainedStack(holder) && ButtonSearch.matchesFilter(stacks.get(0), search.toLowerCase())))) {
                        list.add(holder);
                    } else if (stacks.size() == 0) {
                        HarvestFestival.LOGGER.log(Level.INFO, "Unable to find matching stacks when adding to collections for: " + holder.toString());
                    }
                }

                return list;
            });
        } catch (ExecutionException e) {
            return EMPTY;
        }
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(gui, buttonList, labelList); //Add the tabs
        cache.invalidateAll(); //Clear out the cache
        Keyboard.enableRepeatEvents(true);
        List<AbstractItemHolder> list = getList();
        int j = 0;
        int k = 0;
        int l = 0;
        int added = 0;
        for (int i = (start * 112); added < 112 && i < list.size(); i++) {
            AbstractItemHolder holder = list.get(i);
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

        if (start < list.size() / 112) buttonList.add(new ButtonNext(gui, buttonList.size(), 273, 172));
        if (start != 0) buttonList.add(new ButtonPrevious(gui, buttonList.size(), 20, 172));
        buttonList.add(new ButtonSearch(gui, this, buttonList.size(), 186, 201));
    }

    private boolean hasObtainedStack(AbstractItemHolder holder) {
        for (ItemStack stack : holder.getMatchingStacks()) {
            if (HFTrackers.getClientPlayerTracker().getTracking().hasObtainedItem(stack)) return true;
        }

        return false;
    }
}
