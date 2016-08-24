package joshie.harvest.cooking.gui;

import joshie.harvest.api.cooking.ICookingIngredient;
import joshie.harvest.cooking.CookingHelper;
import joshie.harvest.cooking.FoodRegistry;
import joshie.harvest.cooking.Recipe;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.cooking.packets.PacketSelectRecipe;
import joshie.harvest.core.helpers.ChatHelper;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.util.Text;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static joshie.harvest.cooking.gui.GuiCookbook.LEFT_GUI;
import static joshie.harvest.cooking.gui.GuiCookbook.ingredients;

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
    private final List<CyclingStack> list = new ArrayList<>();
    private final ItemStack stack;
    private final String description;

    public PageRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.stack = recipe.cook(recipe.getMeal());
        this.description = recipe.getRegistryName().getResourceDomain() + ".meal." + recipe.getRegistryName().getResourcePath() + ".description";
    }

    @Override
    public Page getOwner() {
        return PageRecipeList.get(recipe.getRequiredTool());
    }

    public String getRecipeName() {
        return recipe.getDisplayName();
    }

    public ItemStack getItem() {
        return stack;
    }

    @Override
    public Utensil getUtensil() {
        return recipe.getRequiredTool();
    }

    @Override
    public PageRecipe initGui(GuiCookbook gui) {
        super.initGui(gui);
        list.clear();
        int x = 165;
        for (int y = 0; y < recipe.getRequiredIngredients().length; y++) {
            list.add(new CyclingStack(x, 35 + y * 16, recipe.getRequiredIngredients()[y]));
        }

        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int left = ((36-getRecipeName().length()) * 2);
        ItemStack stack = getItem();
        gui.drawString(left, 20, TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + getRecipeName());
        gui.drawBox(25, 30, 110, 1, 0xFFB0A483);
        gui.drawBox(26, 31, 110, 1, 0xFF9C8C63);
        gui.drawString(60, 35, TextFormatting.BOLD + Text.translate("meal.hunger"));
        gui.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
        int hunger = stack.getTagCompound().getInteger("FoodLevel");
        GlStateManager.color(1F, 1F, 1F);
        for (int i = 0; i < hunger; i++) {
            if (i % 2 == 0) {
                gui.drawTexture((i * 4) + 58, 46, 16, 27, 9, 9);
            }

            if (i + 1 < hunger && i % 2 == 0) {
                gui.drawTexture((i * 4) + 58, 46, 52, 27, 9, 9);
            } else if (i % 2 == 0) {
                gui.drawTexture((i * 4) + 58, 46, 61, 27, 9, 9);
            }
        }

        gui.drawBox(25, 60, 110, 1, 0xFFB0A483);
        gui.drawBox(26, 61, 110, 1, 0xFF9C8C63);
        gui.drawString(25, 65, TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + Text.translate("meal.description"));
        gui.drawString(25, 78, Text.localize(description));
        gui.drawStack(22, 30, getItem(), 2F);
        //Bottom

        GlStateManager.enableAlpha();
        //Right Page
        gui.drawString(190, 20, TextFormatting.BOLD + "" + TextFormatting.UNDERLINE + Text.translate("meal.recipe"));
        gui.drawBox(170, 30, 110, 1, 0xFFB0A483);
        gui.drawBox(171, 31, 110, 1, 0xFF9C8C63);
        for (CyclingStack cycling: list) {
            cycling.render(gui);
        }

        //Cook Button
        GlStateManager.color(1F, 1F, 1F);
        gui.mc.getTextureManager().bindTexture(LEFT_GUI);
        int y = mouseX >= 236 && mouseX <= 287 && mouseY >= 148 && mouseY <= 176 ? 129 : 101;
        gui.drawTexture(236, 148, 0, y, 51, 28);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if(mouseX >= 236 && mouseX <= 287 && mouseY >= 148 && mouseY <= 176) {
            if (CookingHelper.hasAllIngredients(recipe, GuiCookbook.ingredients)) {
                String utensil = TextFormatting.YELLOW + PageRecipeList.get(recipe.getRequiredTool()).getItem().getDisplayName() + TextFormatting.RESET;
                String name = TextFormatting.YELLOW + recipe.getDisplayName() + TextFormatting.RESET;
                if (CookingHelper.tryPlaceIngredients(MCClientHelper.getPlayer(), recipe)) {
                    PacketHandler.sendToServer(new PacketSelectRecipe(recipe));
                    MCClientHelper.getPlayer().closeScreen(); //Close this gui
                    ChatHelper.displayChat(TextFormatting.GREEN + Text.translate("meal.success") + TextFormatting.WHITE + " " + Text.format("harvestfestival.meal.success.description", utensil, name));
                } else
                    ChatHelper.displayChat(TextFormatting.RED + Text.translate("meal.failure") + TextFormatting.WHITE + " " + Text.format("harvestfestival.meal.failure.description", utensil, name));
            } else ChatHelper.displayChat(TextFormatting.RED + Text.translate("meal.missing") + TextFormatting.WHITE +  " " + Text.translate("meal.missing.description"));
        }
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

    public static class CyclingStack {
        private final int x;
        private final int y;
        private final List<ItemStack> stacks;
        private final ICookingIngredient ingredient;
        private ItemStack stack;
        private int ticker;
        private int index;

        @SuppressWarnings("unchecked")
        public CyclingStack(int x, int y, ICookingIngredient ingredient) {
            this.x = x;
            this.y = y;
            this.stacks = FoodRegistry.INSTANCE.getStacksForIngredient(ingredient);
            this.ingredient = ingredient;
        }

        public void render(GuiCookbook gui) {
            if (stacks.size() > 0) {
                ticker++;

                if (ticker % 128 == 0 || stack == null) {
                    stack = stacks.get(index); //Pick out the stack
                    index++;
                    if (index >= stacks.size()) {
                        index = 0; //Reset the index
                    }
                }

                gui.drawStack(x, y, stack, 1F);
                boolean isInInventory = CookingHelper.hasIngredientInInventory(ingredients, ingredient);
                TextFormatting formatting = isInInventory ? TextFormatting.DARK_GREEN : TextFormatting.RED;
                gui.drawString(x + 20, y + 6, formatting + stack.getDisplayName());
                GlStateManager.disableDepth();
                gui.mc.getTextureManager().bindTexture(LEFT_GUI);
                if (isInInventory) {
                    gui.drawTexture(x + 8, y + 10, 31, 248, 10, 8);
                } else gui.drawTexture(x + 9, y + 10, 41, 248, 7, 8);

                GlStateManager.enableDepth();
            }
        }
    }
}
