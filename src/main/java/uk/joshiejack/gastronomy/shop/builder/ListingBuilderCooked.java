package uk.joshiejack.gastronomy.shop.builder;

import uk.joshiejack.economy.shop.builder.ListingBuilder;
import uk.joshiejack.gastronomy.cooking.Recipe;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@PenguinLoader("cooked")
public class ListingBuilderCooked extends ListingBuilder<ItemStack> {
    @Override
    public List<String> items() {
        return Recipe.RECIPE_BY_STACK.keySet().stream().filter(k -> !k.getStacks().get(0).isEmpty()).map(key -> StackHelper.getStringFromStack(key.getStacks().get(0))).collect(Collectors.toList());
    }
}
