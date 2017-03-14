package joshie.harvest.debug;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFDebugCommand;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFDebugCommand
public class CommandExportRecipe extends AbstractHFCommand {
    static final HashMap<Recipe, String> DESCRIPTIONS = new HashMap<>();
    static final HashMap<Recipe, String> INFO_OVERRIDE = new HashMap<>();

    @Override
    public String getCommandName() {
        return "export-recipe";
    }

    private Recipe getRecipeFromStack(ItemStack stack) {
        for (Recipe recipe: Recipe.REGISTRY.values()) {
            ItemStack out = recipe.getStack();
            if (out.isItemEqual(stack)) return recipe;
        }

        return null;
    }

    private Recipe getRecipeFromString(String[] name) {
        if (name.length <= 0) return null;
        else return Recipe.REGISTRY.get(new ResourceLocation(MODID, name[0]));
    }

    private int getHunger(Recipe recipe) {
        ArrayList<IngredientStack> stacks = new ArrayList<>();
        stacks.addAll(recipe.getRequired());
        ItemStack stack = RecipeMaker.BUILDER.build(recipe, stacks).get(0);
        return getHunger(stack);
    }

    static int getHunger(ItemStack stack) {
        if (stack.getItem() instanceof ItemFood) {
            return ((ItemFood)(stack.getItem())).getHealAmount(stack);
        }

        return 0;
    }

    private long getCost(Recipe recipe) {
        ArrayList<IngredientStack> stacks = new ArrayList<>();
        stacks.addAll(recipe.getRequired());
        ItemStack stack = RecipeMaker.BUILDER.build(recipe, stacks).get(0);
        return HFApi.shipping.getSellValue(stack);
    }

    private String getUtensilName(Utensil utensil) {
        return utensil.getLocalizedName();
    }

    private String getRecipeList(Recipe recipe) {
        StringBuilder builder = new StringBuilder();
        for (IngredientStack required: recipe.getRequired()) {
            Ingredient ingredient = required.getIngredient();
            builder.append("{{name|");
            builder.append(WordUtils.capitalize(ingredient.getUnlocalized().replace("_", " ")));
            builder.append("}}");
        }

        return builder.toString();
    }

    private String getOptionalList(Recipe recipe) {
        if (recipe.getOptional().size() == 0) return "N/A";
        StringBuilder builder = new StringBuilder();
        for (IngredientStack required: recipe.getOptional()) {
            Ingredient ingredient = required.getIngredient();
            builder.append("{{name|");
            builder.append(WordUtils.capitalize(ingredient.getUnlocalized().replace("_", " ")));
            builder.append("}}");
        }

        return builder.toString();
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        if (sender instanceof EntityPlayer) {
            Recipe recipe = getRecipeFromString(parameters);
            if (recipe == null) {
                ItemStack stack = ((EntityPlayer) sender).getHeldItemMainhand();
                if (stack != null) {
                    recipe = getRecipeFromStack(stack);
                }
            }

            if (recipe != null) {
                StringBuilder builder = new StringBuilder();
                builder.append("{{Infobox recipe\n" + "|image       = ").append(recipe.getDisplayName()).append(".png\n").append("|from        = ").append(getDescription(recipe)).append("\n").append("|hunger = {{hunger|").append(getHunger(recipe)).append("}}\n").append("|price = {{gold|").append(getCost(recipe)).append("}}\n").append("|utensil      = {{name|").append(getUtensilName(recipe.getUtensil())).append("}}\n").append("|recipe   = ").append(getRecipeList(recipe)).append("\n").append("}}");
                String override = INFO_OVERRIDE.containsKey(recipe) ? INFO_OVERRIDE.get(recipe) :
                        recipe.getDisplayName() + " is a cooking recipe made in the [[" + getUtensilName(recipe.getUtensil()) + "]].";
                builder.append("\n").append(override).append("\n").append("\n").append("==Stats==\n").append("{| class=\"wikitable\"\n").append("!Image\n").append("!Name\n").append("!Utensil\n").append("!Required\n").append("!Optional\n").append("!Base Hunger\n").append("!Recipe From\n").append("!Base Sell Price\n").append("|-");
                builder.append("\n|[[File:").append(recipe.getDisplayName().replace(" ", "_")).append(".png]]\n").append("|[[").append(recipe.getDisplayName()).append("]]\n").append("|[[File:").append(getUtensilName(recipe.getUtensil())).append(".png|link=").append(getUtensilName(recipe.getUtensil())).append("]]\n").append("|").append(getRecipeList(recipe)).append("\n").append("|").append(getOptionalList(recipe)).append("\n").append("|{{hunger|").append(getHunger(recipe)).append("}}\n").append("|").append(getDescription(recipe)).append("\n").append("|{{gold|").append(getCost(recipe)).append("}}\n").append("|-\n");

                builder.append("|}");
                ArrayList<IngredientStack> stacks = new ArrayList<>();
                stacks.addAll(recipe.getRequired());
                ItemStack stack = RecipeMaker.BUILDER.build(recipe, stacks).get(0);
                builder.append("\n\n");
                builder.append(CommandGiftExport.getGifts(stack));
                builder.append("\n");
                builder.append(CommandExportUsageInRecipes.getExport(recipe.getStack()));
                builder.append("\n\n{{NavboxRecipes}}[[Category:").append(getUtensilName(recipe.getUtensil())).append(" Recipes]]");
                Debug.save(builder);
            }
        }

        return true;
    }

    private String getDescription(Recipe recipe) {
        if (DESCRIPTIONS.containsKey(recipe)) return DESCRIPTIONS.get(recipe);
        else return "//TODO: Add way to obtain";
    }

    @Override
    public String getUsage() {
        return "";
    }
}
