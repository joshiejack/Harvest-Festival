package joshie.harvest.api.cooking;

import net.minecraft.util.ResourceLocation;

import java.util.*;

public final class Ingredient {
    public static final Map<String, Ingredient> INGREDIENTS = new HashMap<>();
    private final HashSet<Ingredient> equivalents = new HashSet<>();
    private final String unlocalized;
    private int hunger;
    private float saturation;
    private float exhaustion;
    private int eatTime;
    public ResourceLocation fluid;

    /** Creates a new ingredient type for usage in cooking
     *  @param      unlocalized the unlocalised name, this needs to be unique
     *          The food stats are how much this ingredient affects recipes
     *          when it gets added to them as optional ingredients;
     *  @param      hunger the hunger (vanilla) this ingredient fills
     *  @param      saturation the saturation (vanilla) this ingredient fills **/
    public Ingredient(String unlocalized, int hunger, float saturation) {
        this.unlocalized = unlocalized;
        this.hunger = hunger;
        this.saturation = saturation;
        this.exhaustion = 0F;
        this.eatTime = 4;
        equivalents.add(this);
        INGREDIENTS.put(unlocalized, this);
    }

    /** Creates a new cooking category, e.g. "fruit"
     *  To add things to this category, simple call
     *  add(apple, banana, pineapple);
     *  You could recreate this with the newIngredient
     *  by setting stats to 0, but this is for convenience.
     * @param       unlocalized name  */
    public Ingredient(String unlocalized) {
        this.unlocalized = unlocalized;
        equivalents.add(this);
        INGREDIENTS.put(unlocalized, this);
    }

    /** Add additional ingredients as equivalents,
     *  You pretty much use this only on categories ingredients
     * @param ingredients    the ingredients to add*/
    public Ingredient add(Ingredient... ingredients) {
        equivalents.addAll(Arrays.asList(ingredients));
        return this;
    }

    /** If this ingredient should display as a fluid, put the path to that fluid here**/
    public Ingredient setFluid(ResourceLocation fluid) {
        this.fluid = fluid;
        return this;
    }

    /** Set how much exhaustion this add/removes **/
    public Ingredient setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
        return this;
    }

    /** Set how much this ingredient affects the eatimer **/
    public Ingredient setEatTime(int eatTime) {
        this.eatTime = eatTime;
        return this;
    }

    public ResourceLocation getFluid() {
        return fluid;
    }

    public String getUnlocalized() {
        return unlocalized;
    }

    public int getEatTime() {
        return eatTime;
    }

    public int getHunger() {
        return hunger;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getExhaustion() {
        return exhaustion;
    }

    /** With this if you are wanting to test for a category,
     *  this instance should be the categories instance
     *  and the ingredient should be the item you're checking
     *  i.e. this class = juice_vegetable
     *
     * @param ingredient    the ingredient
     * @return if they are equal **/
    public boolean isEqual(Ingredient ingredient) {
        for (Ingredient i : equivalents) { //Return true if the item passed in matches this one
            if (i.getUnlocalized().equals(ingredient.getUnlocalized()))
                return true; //Loops the equivalents list, this item is contained in that list by default
        }

        return false;
    }
}
