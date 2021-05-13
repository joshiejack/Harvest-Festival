package uk.joshiejack.gastronomy.client.gui.pages;

import com.google.common.collect.Lists;
import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.gastronomy.client.gui.GuiCookbook;
import uk.joshiejack.gastronomy.client.gui.buttons.ButtonRecipeListing;
import uk.joshiejack.gastronomy.cooking.Cooker;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.gastronomy.cooking.RecipeBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonForward;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.InventoryHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

@SideOnly(Side.CLIENT)
public class PageRecipeList extends AbstractPageCookbook {
    private final List<PageRecipe> recipeList = Lists.newArrayList();
    private final List<PageRecipe> recipes = Lists.newArrayList();
    public ItemStack icon;
    private int start;

    public PageRecipeList(Appliance appliance) {
        super(appliance);
        Recipe.PRIORITY_RECIPES.get(appliance).forEach((recipe -> recipeList.add(new PageRecipe(appliance, recipe))));
        Recipe.RECIPES.get(appliance).forEach((recipe -> recipeList.add(new PageRecipe(appliance, recipe))));
        this.icon = Cooker.getStackFromAppliance(appliance);
    }

    public boolean hasRecipes() {
        return recipeList.stream().anyMatch((page) -> RecipeBook.hasLearntRecipe(page.getRecipe()));
    }

    @Override
    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
        recipes.clear();
        for (PageRecipe page: recipeList) {
            if (RecipeBook.hasLearntRecipe(page.getRecipe())) recipes.add(page);
        }

        List<IItemHandlerModifiable> inventories = Cooker.getFoodStorageAndPlayer(gui.mc.player);
        NonNullList<ItemStack> stacks = InventoryHelper.getAllStacks(inventories);
        List<FluidStack> fluids = InventoryHelper.getAllFluids(inventories);

        for (int i = 0; i < 10; i++) {
            int index = start + i; //Page One
            if (index < recipes.size()) {
                buttonList.add(new ButtonRecipeListing(recipes.get(index), gui, buttonList.size(), 13, 24 + ((index - start) * 14), "", stacks, fluids));
            }

            //Page Two
            int index2 = start + i + 10;
            if (index2 < recipes.size()) {
                buttonList.add(new ButtonRecipeListing(recipes.get(index2), gui, buttonList.size(), 155, 24 + ((index - start) * 14), "", stacks, fluids));
            }
        }

        int index = start + 20;
        if (index < recipes.size()) {
            buttonList.add(new ButtonForward(this, gui, buttonList.size(), 270, 168));
        }
    }

    @Override
    public boolean hasForwardsButton() {
        return start + 20 < recipes.size();
    }

    @Override
    public void onForward() {
        start = MathsHelper.constrainToRangeInt(start + 20, 0, Integer.MAX_VALUE);
        gui.setPage(this);
    }

    @Override
    public boolean hasBackwardsButton() {
        return true;
    }

    @Override
    public void onBack() {
        if (start != 0) {
            start = MathsHelper.constrainToRangeInt(start - 20, 0, Integer.MAX_VALUE);
            gui.setPage(this);
        } else {
            gui.setPage(GuiCookbook.APPLIANCE_LIST);
        }
    }
}
