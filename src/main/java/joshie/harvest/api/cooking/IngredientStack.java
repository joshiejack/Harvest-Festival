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
        for (Ingredient ingredient: getIngredient().getEquivalents()) {
            if (stack.getIngredient() == ingredient) return true;
        }

        return false;
    }


    /** You should call this as the class being the required item
     *  And we're checking if the match this **/
    public boolean isSame(Collection<IngredientStack> stacks) {
        int count = 0;
        for (IngredientStack stack: stacks) {
            if (isSame(stack)) count++;
        }

        return count >= stackSize;
    }
}