package joshie.harvest.api.cooking;

import net.minecraft.util.ResourceLocation;

import java.util.*;

public final class Ingredient {
    public static final Map<String, Ingredient> INGREDIENTS = new HashMap<>();
    private final Set<Ingredient> equivalents = new HashSet<>();
    private final String unlocalized;
    private int hunger;
    private float saturation;
    private float exhaustion;
    private int eatTime;
    private long value;
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

    /** Called whenever an itemstack is assigned to this ingredient
     *  @param value sell value of the stack that was added **/
    public void onStackAdded(long value) {
        if (this.value == 0) this.value = value;
    }

    /** Set the sell value
     * @param value how much it sells for **/
    public Ingredient setSellValue(long value) {
        this.value = value;
        return this;
    }

    /** Returns the value of this ingredient
     *  Based on the LOWEST value ingredient, used in the recipe */
    public long getSellValue() {
        return value;
    }

    /** Assign an equivalent ingredient to this basic ingredient
     *  @param ingredients  the categories to add **/
    public Ingredient add(Ingredient... ingredients) {
        equivalents.addAll(Arrays.asList(ingredients));
        return this;
    }

    /** If this ingredient should display as a fluid, put the path to that fluid here**/
    public Ingredient setFluid(ResourceLocation fluid) {
        this.fluid = fluid;
        return this;
    }

    @Deprecated //TODO: Remove in 0.7+
    public Ingredient setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
        return this;
    }

    /** Set how much this ingredient affects the eatimer **/
    public Ingredient setEatTime(int eatTime) {
        this.eatTime = eatTime;
        return this;
    }

    /** Return the categories this ingredient fits in to **/
    public Set<Ingredient> getEquivalents() {
        return equivalents;
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

    @Deprecated //TODO: Remove in 0.7+
    public float getExhaustion() {
        return exhaustion;
    }

    @Deprecated //TODO: Remove in 0.7+
    public boolean isEqual(Ingredient ingredient) {
        for (Ingredient i : equivalents) { //Return true if the item passed in matches this one
            if (i.getUnlocalized().equals(ingredient.getUnlocalized()))
                return true; //Loops the equivalents list, this item is contained in that list by default
        }

        return false;
    }
}
