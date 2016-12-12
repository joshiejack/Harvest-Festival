package joshie.harvest.debug;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.recipe.RecipeMaker;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static joshie.harvest.core.lib.HFModInfo.MODID;

@HFCommand
public class CommandExportRecipe extends AbstractHFCommand {
    static HashMap<Recipe, String> DESCRIPTIONS = new HashMap<>();
    static HashMap<Recipe, String> INFO_OVERRIDE = new HashMap<>();

    @Override
    public String getCommandName() {
        return "export-recipe";
    }

    private Recipe getRecipeFromStack(ItemStack stack) {
        for (Recipe recipe: Recipe.REGISTRY) {
            ItemStack out = recipe.getStack();
            if (out.isItemEqual(stack)) return recipe;
        }

        return null;
    }

    private Recipe getRecipeFromString(String[] name) {
        if (name.length <= 0) return null;
        else return Recipe.REGISTRY.getValue(new ResourceLocation(MODID, name[0]));
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
        String name = ReflectionHelper.getPrivateValue(Utensil.class, utensil, "label");
        return WordUtils.capitalize(name.replace("_", " ").toLowerCase());
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
                builder.append("{{Infobox recipe\n" +
                        "|image       = " + recipe.getDisplayName() + ".png\n" +
                        "|from        = " + getDescription(recipe) + "\n" +
                        "|hunger = {{hunger|" + getHunger(recipe) + "}}\n" +
                        "|price = {{gold|" + getCost(recipe) + "}}\n" +
                        "|utensil      = {{name|" + getUtensilName(recipe.getUtensil()) + "}}\n" +
                        "|recipe   = " + getRecipeList(recipe) + "\n" +
                        "}}");
                String override = INFO_OVERRIDE.containsKey(recipe) ? INFO_OVERRIDE.get(recipe) :
                        recipe.getDisplayName() + " is a cooking recipe made in the [[" + getUtensilName(recipe.getUtensil()) + "]].";
                builder.append("\n" + override + "\n" +
                        "\n" +
                        "==Stats==\n" +
                        "{| class=\"wikitable\"\n" +
                        "!Image\n" +
                        "!Name\n" +
                        "!Utensil\n" +
                        "!Required\n" +
                        "!Optional\n" +
                        "!Base Hunger\n" +
                        "!Recipe From\n" +
                        "!Base Sell Price\n" +
                        "|-");
                builder.append("\n|[[File:" + recipe.getDisplayName().replace(" ", "_") + ".png]]\n" +
                        "|[[" + recipe.getDisplayName() + "]]\n" +
                        "|[[File:" + getUtensilName(recipe.getUtensil()) + ".png|link=" + getUtensilName(recipe.getUtensil()) + "]]\n" +
                        "|" + getRecipeList(recipe) + "\n" +
                        "|" + getOptionalList(recipe) + "\n" +
                        "|{{hunger|" + getHunger(recipe) + "}}\n" +
                        "|" + getDescription(recipe) + "\n" +
                        "|{{gold|" + getCost(recipe) + "}}\n" +
                        "|-\n");

                builder.append("|}");
                ArrayList<IngredientStack> stacks = new ArrayList<>();
                stacks.addAll(recipe.getRequired());
                ItemStack stack = RecipeMaker.BUILDER.build(recipe, stacks).get(0);
                builder.append("\n\n");
                builder.append(CommandGiftExport.getGifts(stack));
                builder.append("\n");
                builder.append(CommandExportUsageInRecipes.getExport(recipe.getStack()));
                builder.append("\n\n{{NavboxRecipes}}[[Category:" + getUtensilName(recipe.getUtensil()) + " Recipes]]");
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
