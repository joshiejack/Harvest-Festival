package joshie.harvest.api.cooking;

import java.util.Collection;

public final class IngredientStack {
    private final Ingredient ingredient;
    private final int stackSize;

    public IngredientStack(Ingredient ingredient) {
        this(ingredient, 1);
    }

    public IngredientStack(Ingredient ingredient, int stackSize) {
        this.ingredient = ingredient;
        this.stackSize = stackSize;
        if (ingredient == null) {
            throw new NullPointerException("Attempted to add a null ingredient to an ingredient stack");
        }
    }

    /** returns the stack size **/
    public int getStackSize() {
        return stackSize;
    }

    /** Returns the ingredient **/
    public Ingredient getIngredient() {
        return ingredient;
    }

    /** You should call this as the class being the required item
     *  And we're checking if the match this **/
    public boolean isSame(IngredientStack stack) {
        for (Ingredient ingredient: stack.ingredient.getEquivalents()) {
            if (getIngredient() == ingredient) return true;
        }

        return false;
    }


    /** You should call this as the class being the required item
     *  And we're checking if the match this **/
    public boolean isSame(Collection<IngredientStack> stacks) {
        for (IngredientStack stack: stacks) {
            if (isSame(stack)) return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientStack that = (IngredientStack) o;
        return ingredient != null ? ingredient.equals(that.ingredient) : that.ingredient == null;

    }

    @Override
    public int hashCode() {
        return ingredient != null ? ingredient.hashCode() : 0;
    }
}
