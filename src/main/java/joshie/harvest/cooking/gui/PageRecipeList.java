package joshie.harvest.cooking.gui;

import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.cooking.recipe.MealImpl;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static joshie.harvest.cooking.HFCooking.COOKWARE;
import static joshie.harvest.cooking.block.BlockCookware.Cookware.*;
import static joshie.harvest.cooking.gui.GuiCookbook.MASTER;
import static joshie.harvest.cooking.gui.GuiCookbook.MAX_UTENSILS_DISPLAY;

/** Display the recipe list **/
public class PageRecipeList extends Page {
    private static final HashMap<Utensil, PageRecipeList> UTENSIL_PAGES = new HashMap<>();
    private static final HashMap<Utensil, ItemStack> RENDER_MAP = new HashMap<>();
    static {
        RENDER_MAP.put(Utensil.COUNTER, COOKWARE.getStackFromEnum(COUNTER));
        RENDER_MAP.put(Utensil.FRYING_PAN, COOKWARE.getStackFromEnum(FRYING_PAN));
        RENDER_MAP.put(Utensil.MIXER, COOKWARE.getStackFromEnum(MIXER));
        RENDER_MAP.put(Utensil.OVEN, COOKWARE.getStackFromEnum(OVEN_ON));
        RENDER_MAP.put(Utensil.POT, COOKWARE.getStackFromEnum(POT));
        for (int i = 0; i < MAX_UTENSILS_DISPLAY; i++) {
            Utensil utensil = Utensil.getUtensilFromIndex(i);
            UTENSIL_PAGES.put(utensil, new PageRecipeList(utensil));
        }
    }

    public static PageRecipeList get(Utensil utensil) {
        return UTENSIL_PAGES.get(utensil);
    }

    private List<PageRecipe> recipes;
    private Utensil utensil;

    private PageRecipeList(Utensil utensil) {
        this.utensil = utensil;
    }

    public PageRecipeList initGui(GuiCookbook gui) {
        super.initGui(gui);
        recipes = new ArrayList<>();
        for (ResourceLocation resource: HFTrackers.getClientPlayerTracker().getTracking().getLearntRecipes()) {
            MealImpl recipe = CookingAPI.REGISTRY.getObject(resource);
            if (recipe.getRequiredTool() == utensil) {
                recipes.add(PageRecipe.of(recipe));
            }
        }

        return this;
    }

    @Override
    public Page getOwner() {
        return MASTER;
    }

    @Override
    public Utensil getUtensil() {
        return utensil;
    }

    public ItemStack getItem() {
        return RENDER_MAP.get(utensil);
    }

    public boolean hasRecipes() {
        return recipes.size() > 0;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        //Draw the left hand side
        int maxLeft = Math.min(10, recipes.size());
        if (maxLeft > 0) {
            boolean hoverX = mouseX >= 25 && mouseX <= 135;
            for (int i = 0; i < maxLeft; i++) {
                PageRecipe recipe = recipes.get(i);
                boolean hoverY = mouseY >= 22 + i * 14 && mouseY <= 35 + i * 14;
                if (hoverX && hoverY) {
                    gui.drawString(45, 24 + i * 14, TextFormatting.ITALIC + recipe.getRecipeName());
                } else gui.drawString(45, 24 + i * 14, recipe.getRecipeName());

                gui.drawStack(25, 20 + i * 14, recipe.getItem(), 1F);
            }
        }

        //Optionally draw the right hand side
        if (maxLeft == 10 && recipes.size() > 10) {
            boolean hoverX = mouseX >= 170 && mouseX <= 285;
            for (int j = 10; j < recipes.size(); j++) {
                PageRecipe recipe = recipes.get(j);
                int i = j - 10;
                boolean hoverY = mouseY >= 22 + i * 14 && mouseY <= 35 + i * 14;
                if (hoverX && hoverY) {
                    gui.drawString(190, 24 + i * 14, TextFormatting.ITALIC + recipe.getRecipeName());
                } else gui.drawString(190, 24 + i * 14, recipe.getRecipeName());

                gui.drawStack(170, 20 + i * 14, recipe.getItem(), 1F);
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
        //Click the left hand side
        int maxLeft = Math.min(10, recipes.size());
        if (maxLeft > 0) {
            boolean hoverX = mouseX >= 25 && mouseX <= 135;
            for (int i = 0; i < maxLeft; i++) {
                boolean hoverY = mouseY >= 22 + i * 14 && mouseY <= 35 + i * 14;
                if (hoverX && hoverY) {
                    return gui.setPage(recipes.get(i));
                }
            }
        }

        //Optionally draw the right hand side
        if (maxLeft == 10 && recipes.size() > 10) {
            boolean hoverX = mouseX >= 170 && mouseX <= 285;
            for (int j = 10; j < recipes.size(); j++) {
                int i = j - 10;
                boolean hoverY = mouseY >= 22 + i * 14 && mouseY <= 35 + i * 14;
                if (hoverX && hoverY) {
                    return gui.setPage(recipes.get(j));
                }
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageRecipeList that = (PageRecipeList) o;
        return utensil == that.utensil;

    }

    @Override
    public int hashCode() {
        return utensil != null ? utensil.hashCode() : 0;
    }
}
