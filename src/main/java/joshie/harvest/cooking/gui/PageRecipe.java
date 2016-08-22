package joshie.harvest.cooking.gui;

import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.cooking.Recipe;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class PageRecipe extends Page {
    private static final HashMap<Recipe, PageRecipe> recipeMap = new HashMap<>();
    static {
        for (Recipe recipe: FoodRegistry.REGISTRY) {
            recipeMap.put(recipe, new PageRecipe(recipe));
        }
    }

    public static PageRecipe of(Recipe recipe) {
        return recipeMap.get(recipe);
    }

    private final Recipe recipe;

    public PageRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public Page getOwner() {
        return PageRecipeList.get(recipe.getRequiredTool());
    }

    public String getRecipeName() {
        return recipe.getDisplayName();
    }

    public ItemStack getItem() {
        return recipe.cook(recipe.getMeal());
    }

    @Override
    public void draw(int mouseX, int mouseY) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageRecipe that = (PageRecipe) o;
        return recipe != null ? recipe.equals(that.recipe) : that.recipe == null;
    }

    @Override
    public int hashCode() {
        return recipe != null ? recipe.hashCode() : 0;
    }
}
