package uk.joshiejack.gastronomy.cooking;

import com.google.common.collect.Sets;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import java.util.Collection;

public class IngredientStack {
    private final NonNullList<ItemStack> stacks = NonNullList.create();
    private final Collection<Category> categories;
    private final Ingredient ingredient;
    private final boolean isFluid;
    private int amount;

    public IngredientStack(Ingredient ingredient, int amount) {
        this.ingredient = ingredient;
        this.categories = Category.getCategories(ingredient);
        this.amount = amount;
        this.isFluid = false;
    }

    public IngredientStack(Category category, int amount) {
        this.ingredient = Ingredient.NONE;
        this.categories = Sets.newHashSet(category);
        this.amount = amount;
        this.isFluid = false;
    }

    public IngredientStack(FluidStack fluid) {
        this.ingredient = Ingredient.getIngredient(fluid.getFluid().getName());
        this.categories = Category.getCategories(this.ingredient);
        this.amount = fluid.amount;
        this.isFluid = true;
    }

    public boolean isFluid() {
        return isFluid;
    }

    public boolean isEmpty() {
        return amount == 0 || ingredient.isNone();
    }

    public int getAmount() {
        return amount;
    }

    public void shrink(int amount) {
        if (isFluid) {
            this.amount -= (125 * amount);
        } else this.amount -= amount;
    }

    //INPUT.matches(REQUIREMENT);
    public boolean matches(IngredientStack requirement) {
        return (!requirement.ingredient.isNone() && ingredient == requirement.ingredient) || (requirement.ingredient.isNone() && categories.stream().anyMatch(requirement.categories::contains));
    }

    public NonNullList<ItemStack> getAsStacks() {
        if (stacks.isEmpty()) {
            if (!ingredient.isNone()) stacks.addAll(ingredient.getStacks());
            else categories.forEach(category -> stacks.addAll(category.getStacks()));
            if (stacks.isEmpty() && !ingredient.isNone()) {
                //Probably was a fluid instead so we want to grab all
                for (ItemStack stack : StackHelper.getAllItemsCopy()) {
                    FluidStack fluid = FluidUtil.getFluidContained(stack);
                    if (fluid != null && ingredient.name.equals(fluid.getFluid().getName())) {
                        stacks.add(stack);
                    }
                }
            }
        }

        return stacks;
    }
}
