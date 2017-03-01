package joshie.harvest.knowledge.gui.stats.collection.page;

import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.knowledge.gui.stats.GuiStats;
import joshie.harvest.knowledge.gui.stats.button.ButtonNext;
import joshie.harvest.knowledge.gui.stats.button.ButtonPrevious;
import joshie.harvest.knowledge.gui.stats.collection.button.ButtonShipped;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;

@SuppressWarnings("WeakerAccess")
public class PageCooking extends PageCollection {
    public static final BookPage INSTANCE = new PageCooking();

    private PageCooking() {
        super("cooking", HFCooking.MEAL.getStackFromEnum(Meal.SALAD));
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList, List<GuiLabel> labelList) {
        super.initGui(gui, buttonList, labelList); //Add the tabs
        List<Recipe> list = new ArrayList<>(Recipe.REGISTRY.values());
        int j = 0;
        int k = 0;
        int l = 0;
        int added = 0;
        for (int i = start * 112; added < start * 112 + 112 && i < list.size(); i++) {
            Recipe recipe = list.get(i);
            ArrayList<IngredientStack> stacks = new ArrayList<>();
            stacks.addAll(recipe.getRequired());
            ItemStack stack = RecipeMaker.BUILDER.build(recipe, stacks).get(0);
            stack.stackSize = 1;
            long value = stack.getTagCompound() != null ? stack.getTagCompound().getLong(SELL_VALUE): 0L;
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
    }

    private boolean hasObtainedStack(ItemStack stack) {
        return HFTrackers.getClientPlayerTracker().getTracking().hasObtainedItem(stack);
    }
}
