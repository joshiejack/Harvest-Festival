package joshie.harvest.api.cooking;

import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recipe extends IForgeRegistryEntry.Impl<Recipe> {
    public static final IForgeRegistry<Recipe> REGISTRY = new RegistryBuilder<Recipe>().setName(new ResourceLocation("harvestfestival", "meals")).setType(Recipe.class).setIDRange(0, 32000).create();

    private final List<IngredientStack> required = new ArrayList<>();
    private List<IngredientStack> optional = new ArrayList<>();
    private final Utensil utensil;
    private int hunger;
    private float saturation;
    private EnumAction action;
    private int eatTimer;
    private int maximumOptional;
    private boolean isLearntByDefault;

    public Recipe(Utensil utensil, IngredientStack... required) {
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

    @SuppressWarnings("deprecation")
    public String getDisplayName() {
        return I18n.translateToLocal(getRegistryName().getResourceDomain() + ".meal." + getRegistryName().getResourcePath().replace("_", "."));
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

    public ItemStack getStack() {
        return new ItemStack(Items.CAKE);
    }

    public boolean supportsNBTData() {
        return true;
    }
}
