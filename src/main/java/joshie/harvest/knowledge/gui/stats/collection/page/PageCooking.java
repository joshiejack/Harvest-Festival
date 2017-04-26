package joshie.harvest.knowledge.gui.stats.collection.page;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonNext;
import joshie.harvest.knowledge.gui.stats.button.ButtonPrevious;
import joshie.harvest.knowledge.gui.stats.collection.button.ButtonSearch;
import joshie.harvest.knowledge.gui.stats.collection.button.ButtonShipped;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class PageCooking extends PageCollection {
    public static final BookPage INSTANCE = new PageCooking();
    private final Cache<String, List<Recipe>> cache = CacheBuilder.newBuilder().maximumSize(64).build();
    private static final List<Recipe> EMPTY = Lists.newArrayList();

    private PageCooking() {
        super("cooking", HFCooking.MEAL.getStackFromEnum(Meal.SALAD));
    }

    private List<Recipe> getList() {
        try {
            return cache.get(search, () -> {
                List<Recipe> list = new ArrayList<>();
                for (Recipe recipe : Recipe.REGISTRY.values()) {
                    ItemStack stack = RecipeMaker.BUILDER.build(recipe, Lists.newArrayList(recipe.getRequired())).get(0);
                    if ((Strings.isNullOrEmpty(search) || (hasObtainedStack(stack) && ButtonSearch.matchesFilter(stack, search.toLowerCase())))) {
                        list.add(recipe);
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
        List<Recipe> list = getList();
        int j = 0;
        int k = 0;
        int l = 0;
        int added = 0;
        for (int i = start * 112; added < start * 112 + 112 && i < list.size(); i++) {
            Recipe recipe = list.get(i);
            ItemStack stack = RecipeMaker.BUILDER.build(recipe, Lists.newArrayList(recipe.getRequired())).get(0);
            stack.setCount(1); //Force a stacksize of one for rendering purposes
            long value = HFApi.shipping.getSellValue(stack);
            boolean obtained = hasObtainedStack(stack);
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
            buttonList.add(new ButtonShipped(gui, stack, value, obtained, buttonList.size(), l + 3 + k * 18, 24 + j * 18));
        }

        int maxStart = list.size() / 112;
        if (start < maxStart) buttonList.add(new ButtonNext(gui, buttonList.size(), 273, 172));
        if (start != 0) buttonList.add(new ButtonPrevious(gui, buttonList.size(), 20, 172));
        buttonList.add(new ButtonSearch(gui, this, buttonList.size(), 186, 201));
    }

    private boolean hasObtainedStack(@Nonnull ItemStack stack) {
        return HFTrackers.getClientPlayerTracker().getTracking().hasObtainedItem(stack);
    }
}
