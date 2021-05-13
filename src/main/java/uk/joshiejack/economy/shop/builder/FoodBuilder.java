package uk.joshiejack.economy.shop.builder;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

@PenguinLoader("food")
public class FoodBuilder extends ListingBuilder<ItemStack> {
    @Override
    public List<String> items() {
        return StackHelper.getAllItems().stream().filter(stack -> stack.getItem() instanceof ItemFood).map(StackHelper::getStringFromStack).collect(Collectors.toList());
    }
}
