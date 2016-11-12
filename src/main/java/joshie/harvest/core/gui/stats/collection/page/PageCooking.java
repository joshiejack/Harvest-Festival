package joshie.harvest.core.gui.stats.collection.page;

import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.gui.BookPage;
import joshie.harvest.core.gui.stats.GuiStats;
import joshie.harvest.core.gui.stats.collection.button.ButtonShipped;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static joshie.harvest.core.registry.ShippingRegistry.SELL_VALUE;

public class PageCooking extends PageCollection {
    public static final BookPage INSTANCE = new PageCooking();

    private PageCooking() {
        super("cooking", HFCooking.MEAL.getStackFromEnum(Meal.SALAD));
    }

    @Override
    public void initGui(GuiStats gui, List<GuiButton> buttonList) {
        super.initGui(gui, buttonList); //Add the tabs
        List<Recipe> list = Recipe.REGISTRY.getValues();
        int j = 0;
        int k = 0;
        int l = 0;
        int added = 0;
        for (int i = 0; added <= 144 && i < list.size(); i++) {
            Recipe recipe = list.get(i);
            ArrayList<IngredientStack> stacks = new ArrayList<>();
            stacks.addAll(recipe.getRequired());
            ItemStack stack = RecipeMaker.BUILDER.build(recipe, stacks).get(0);
            stack.stackSize = 1;
            long value = stack.hasTagCompound() ? stack.getTagCompound().getLong(SELL_VALUE): 0L;
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
    }

    private boolean hasObtainedStack(ItemStack stack) {
        return HFTrackers.getClientPlayerTracker().getTracking().hasObtainedItem(stack);
    }
}
