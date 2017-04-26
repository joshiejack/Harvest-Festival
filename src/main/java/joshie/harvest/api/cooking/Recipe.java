package joshie.harvest.api.cooking;

import joshie.harvest.api.core.HFRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;
import java.util.*;

//TODO: Remove forge registry in 0.7+
//Do not call setRegistryName or anything
//This is only extending the old forge registry for 0.5 > 0.6 compatability reason
public class Recipe extends HFRegistry<Recipe> {
    public static final Map<ResourceLocation, Recipe> REGISTRY = new HashMap<>();
    private final List<IngredientStack> required = new ArrayList<>();
    private List<IngredientStack> optional = new ArrayList<>();
    private Utensil utensil;
    private int hunger;
    private float saturation;
    private EnumAction action;
    private int eatTimer;
    private int maximumOptional;
    private boolean isLearntByDefault;

    public Recipe(ResourceLocation resource, Utensil utensil, IngredientStack... required) {
        super(resource);
        this.utensil = utensil;
        this.action = EnumAction.EAT;
        this.eatTimer = 24;
        this.maximumOptional = 20;
        Collections.addAll(this.required, required);
    }

    public void setDefault() {
        isLearntByDefault = true;
    }

    public Recipe setFoodStats(int hunger, float saturation) {
        this.hunger = hunger;
        this.saturation = saturation;
        return this;
    }

    @SuppressWarnings("unused")
    public Recipe setRequiredIngredients(IngredientStack... required) {
        Collections.addAll(this.required, required);
        return this;
    }

    public Recipe setOptionalIngredients(Ingredient... ingredients) {
        for (Ingredient ingredient: ingredients) {
            this.optional.add(new IngredientStack(ingredient));
        }

        return this;
    }

    @SuppressWarnings("unused")
    public Recipe setOptionalIngredients(IngredientStack... ingredients) {
        Collections.addAll(this.optional, ingredients);
        return this;
    }

    public Recipe setIsDrink() {
        this.action = EnumAction.DRINK;
        return this;
    }

    public Recipe setEatTimer(int eatTimer) {
        this.eatTimer = eatTimer;
        return this;
    }

    public Recipe setMaximumOptionalIngredients(int maximum) {
        this.maximumOptional = maximum;
        return this;
    }

    @Override
    public final Map<ResourceLocation, Recipe> getRegistry() {
        return REGISTRY;
    }

    @SuppressWarnings("deprecation")
    public String getDisplayName() {
        return I18n.translateToLocal(getResource().getResourceDomain() + ".meal." + getResource().getResourcePath().replace("_", "."));
    }

    public boolean isDefault() {
        return isLearntByDefault;
    }

    public List<IngredientStack> getRequired() {
        return required;
    }

    public List<IngredientStack> getOptional() {
        return optional;
    }

    public int getMaximumOptionalIngredients() {
        return maximumOptional;
    }

    public Utensil getUtensil() {
        return utensil;
    }

    public int getHunger() {
        return hunger;
    }

    public float getSaturation() {
        return saturation;
    }

    public EnumAction getAction() {
        return action;
    }

    public int getEatTimer() {
        return eatTimer;
    }

    @Nonnull
    public ItemStack getStack() {
        return new ItemStack(Items.CAKE);
    }

    public boolean supportsNBTData() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof Recipe && getResource().equals(((Recipe) o).getResource());
    }

    @Override
    public int hashCode() {
        return getResource().hashCode();
    }
}