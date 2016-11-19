package joshie.harvest.debug;

import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.CookingAPI;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.text.WordUtils;

import static joshie.harvest.debug.CommandExportRecipe.DESCRIPTIONS;

@HFCommand
public class CommandExportUsageInRecipes extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "export-usage";
    }

    @Override
    public String getUsage() {
        return "";
    }

    public static StringBuilder getExport(ItemStack exporting) {
        Ingredient theIngredient = CookingAPI.INSTANCE.getCookingComponents(exporting);
        if (theIngredient != null) {
            StringBuilder builder = new StringBuilder();
            builder.append("==Recipes==\n" +
                    "{| class=\"wikitable sortable\"\n" +
                    "!Image\n" +
                    "!Name\n" +
                    "!Utensil\n" +
                    "!Required\n" +
                    "!Optional\n" +
                    "!Recipe From\n" +
                    "|-");
            IngredientStack istacks = new IngredientStack(theIngredient);
            for (Recipe recipe : Recipe.REGISTRY) {
                boolean doThis = false;
                for (IngredientStack iStack : recipe.getRequired()) {
                    if (iStack.isSame(istacks)) {
                        doThis = true;
                        break;
                    }
                }

                if (doThis) {
                    builder.append("\n|[[File:");
                    builder.append(recipe.getDisplayName());
                    builder.append(".png]]");
                    builder.append("\n|[[");
                    builder.append(recipe.getDisplayName());
                    builder.append("]]");
                    builder.append("\n|[[File:");
                    String label = ReflectionHelper.getPrivateValue(Utensil.class, recipe.getUtensil(), "label");
                    builder.append(WordUtils.capitalize(label.replace("_", " ")).replace(" ", "_"));
                    builder.append(".png|link=");
                    builder.append(WordUtils.capitalize(label.replace("_", " ")));
                    builder.append("]]");
                    builder.append("\n|");
                    for (IngredientStack required : recipe.getRequired()) {
                        Ingredient ingredient = required.getIngredient();
                        builder.append("{{name|");
                        builder.append(WordUtils.capitalize(ingredient.getUnlocalized().replace("_", " ")));
                        builder.append("}}");
                    }

                    if (recipe.getOptional().size() == 0) {
                        builder.append("\n|N/A");
                    } else {
                        builder.append("\n|");
                        for (IngredientStack required : recipe.getOptional()) {
                            Ingredient ingredient = required.getIngredient();
                            builder.append("{{name|");
                            builder.append(WordUtils.capitalize(ingredient.getUnlocalized().replace("_", " ")));
                            builder.append("}}");
                        }
                    }

                    String name = DESCRIPTIONS.containsKey(recipe) ? DESCRIPTIONS.get(recipe) : "//TODO: Add way to obtain";
                    builder.append("\n|" + name + "\n" +
                            "|-");
                }
            }

            builder.append("\n|}");
            return builder;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("==Recipes==\nUsed in no recipes");
        return builder;
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        return false;
    }
}
