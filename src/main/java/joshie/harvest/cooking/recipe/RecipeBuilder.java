package joshie.harvest.cooking.recipe;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import joshie.harvest.api.cooking.Ingredient;
import joshie.harvest.api.cooking.IngredientStack;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.core.helpers.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class RecipeBuilder {
    public static final String FOOD_LEVEL = "FoodLevel";
    public static final String SATURATION_LEVEL = "FoodSaturation";
    public static final String SELL_VALUE = "SellValue";
    private List<IngredientStack> required;
    private List<IngredientStack> optional;
    private int hunger = 0;
    private float saturation;
    private int stackSize;
    private long cost;

    public List<ItemStack> build(Recipe recipe, List<IngredientStack> ingredients) {
        //We can and will end up with recipes in the required and optional list
        required = toRequired.toList(recipe, ingredients);
        optional = toOptional.toList(recipe, ingredients);
        stackSize = 1; //Always create one recipe at least
        //We first always make one required item, and add all the optionals to it, as we already know we CAN make one item
        Set<Ingredient> removed = new HashSet<>();
        for (IngredientStack stack: recipe.getRequired()) {
            if (!removed.contains(stack.getIngredient())) {
                removed.add(stack.getIngredient());
                removeFromList(stack);
            }
        }

        //The next step is calculate the additional hunger, and saturation from the optional list
        calculateHungerSaturationAndRemoveOptionals(recipe, new ArrayList<>(optional));
        //Now that we pretty much only have the spare items left in the required list
        //We need to work out how many items total we can make, as well as calculating any extra
        //To add to the last item in our stack
        calculateStackSizeBasedOnRequiredListAndRemainingItems(recipe.getRequired());
        calculateActualHungerAndSaturationValues(recipe);
        calculateCostsBasedOnEverything(recipe);
        //We now know exactly what the stack size will be, as well as exactly how many items we have left!
        //We know this, as it will be the items remaining in the required
        //If we have stuff to calculate then do so, otherwise return everything as is
        if (required.size() == 0 || recipe.supportsNBTData()) return build(recipe.getStack(), recipe.supportsNBTData());
        else {
            //Now we need to work out the additional stats for the last bit of food
            List<ItemStack> ret = new ArrayList<>();
            if (stackSize > 1) ret.add(setFoodStats(StackHelper.toStack(recipe.getStack(), stackSize - 1), hunger, saturation, cost));
            //Added the basic recipes, now we need to work out the final oddball stack to add
            float multiplier = 1F;
            for (int i = 1; i <= required.size(); i++) {
                multiplier += 0.5F / i;
            }

            //Now that we calulcated the multiplier let's adjust the values
            hunger = (int)Math.floor((double)hunger * multiplier);
            saturation = saturation * multiplier;
            ret.add(setFoodStats(recipe.getStack(), hunger, saturation, cost));
            return ret;
        }
    }

    private List<ItemStack> build(ItemStack basicStack, boolean supportsNBTData) {
        if (!supportsNBTData) return Collections.singletonList(StackHelper.toStack(basicStack, stackSize));
        else return Collections.singletonList(setFoodStats(StackHelper.toStack(basicStack, stackSize), hunger, saturation, cost));
    }

    @SuppressWarnings("ConstantConditions")
    public static ItemStack setFoodStats(ItemStack stack, int hunger, float saturation, long cost) {
        if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger(FOOD_LEVEL, hunger);
        tag.setFloat(SATURATION_LEVEL, saturation);
        tag.setLong(SELL_VALUE, cost);
        return stack;
    }

    private void calculateCostsBasedOnEverything(Recipe recipe) {
        long sell = 0L;
        for (IngredientStack stack: recipe.getRequired()) {
            long internal = stack.getIngredient().getSellValue();
            if (internal == 0) {
                for (Ingredient ingredient: stack.getIngredient().getEquivalents()) {
                    if (internal == 0 || ingredient.getSellValue() < internal) {
                        internal = ingredient.getSellValue();
                    }
                }

                sell += internal;
            } else sell += stack.getIngredient().getSellValue();
        }

        this.cost = (long)(sell * 1.12);
    }

    private void calculateActualHungerAndSaturationValues(Recipe recipe) {
        //Add the leftover required ingredients
        if (required.size() > 0) {
            TObjectIntMap<Ingredient> added = new TObjectIntHashMap<>();
            for (IngredientStack stack: required) {
                Ingredient main = stack.getIngredient();
                //If we haven't already added this ingredient, use the full value
                if (!added.containsKey(main)) {
                    hunger += (main.getHunger() / 2);
                    saturation += (main.getSaturation() / 2);
                    added.put(main, 0);
                } else {
                    //If we have already used this ingredient, let's get how many times we have
                    int used = added.adjustOrPutValue(main, 1, 1);
                    hunger += (((double)main.getHunger())/ (4 * used));
                    saturation += ((main.getSaturation())/ (4 * used));
                    //We're added less and less each time to hunger and saturation for each ingredient
                }
            }
        }

        this.hunger = recipe.getHunger() + (int)((double)this.hunger / stackSize);
        this.saturation = recipe.getSaturation() + (this.saturation / stackSize);
    }

    private void calculateStackSizeBasedOnRequiredListAndRemainingItems(List<IngredientStack> required) {
        int totalRecipesMade = 0;
        for (int i = 0; i < 64; i++) {
            if (areAllRequiredInRecipeAndRemove(required)) {
                totalRecipesMade++;
            } else break;
        }

        this.stackSize += totalRecipesMade;
    }

    private void removeFromList(IngredientStack required) {
        //Search through the required set, and remove the first entry we find that matches
        int removed = 0;
        Iterator<IngredientStack> it = this.required.iterator();
        while(it.hasNext()) {
            if (required.isSame(it.next())) {
                it.remove();
                removed++;
                if (removed >= required.getStackSize()) {
                    break;
                }
            }
        }
    }

    private boolean areAllRequiredInRecipeAndRemove(List<IngredientStack> requiredSet) {
        //We have looped through the set, so we should now go through and remove from the required again
        for (IngredientStack required: requiredSet) {
            if (!required.isSame(this.required)) return false;
        }

        //Removed and cleared up the set so we carry on
        for (IngredientStack required: requiredSet) {
            removeFromList(required);
        }

        return true;
    }

    private void calculateHungerSaturationAndRemoveOptionals(Recipe recipe, List<IngredientStack> optionals) {
        float hunger = 0;
        float saturation = 0F;
        TObjectIntMap<Ingredient> added = new TObjectIntHashMap<>();
        for (IngredientStack stack: optionals) {
            Ingredient main = stack.getIngredient();
            //If we haven't already added this ingredient, use the full value
            if (!added.containsKey(main)) {
                hunger += main.getHunger();
                saturation += main.getSaturation();
                added.put(main, 0);
            } else {
                //If we have already used this ingredient, let's get how many times we have
                int used = added.adjustOrPutValue(main, 1, 1);
                hunger += (((double)main.getHunger())/ (4 * used));
                saturation += ((main.getSaturation())/ (4 * used));
                //We're added less and less each time to hunger and saturation for each ingredient
            }

            if (added.size() >= recipe.getMaximumOptionalIngredients()) break;
        }

        this.optional.clear();
        this.hunger = (int) Math.floor(hunger);
        this.saturation = saturation;
    }

    private abstract class ListBuilder {
        List<IngredientStack> toList(Recipe recipe, List<IngredientStack> ingredients) {
            List<IngredientStack> list = new ArrayList<>();
            for (IngredientStack required: getList(recipe)) {
                //Search through the required set, and remove the first entry we find that matches
                Iterator<IngredientStack> it = ingredients.iterator();
                while(it.hasNext()) {
                    IngredientStack stack = it.next();
                    if (required.isSame(stack)) {
                        list.add(stack);
                    }
                }
            }

            return list;
        }

        protected abstract List<IngredientStack> getList(Recipe recipe);
    }

    private final ListBuilder toRequired = new ListBuilder() {
        @Override
        protected List<IngredientStack> getList(Recipe recipe) {
            return recipe.getRequired();
        }
    };

    private final ListBuilder toOptional = new ListBuilder() {
        @Override
        protected List<IngredientStack> getList(Recipe recipe) {
            return recipe.getOptional();
        }
    };
}
