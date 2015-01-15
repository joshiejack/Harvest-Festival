package joshie.harvestmoon.cooking;

import java.util.ArrayList;

import joshie.harvestmoon.util.SafeStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SavedRecipe {
    public RecipeType type = RecipeType.SNACK;
    public Utensil tool = Utensil.KITCHEN;
    public ArrayList<Ingredient> ingredients = new ArrayList();
    public ArrayList<Seasoning> seasonings = new ArrayList();

    public SavedRecipe() {}
    public SavedRecipe(RecipeType type, Utensil tool, ArrayList<Ingredient> ingredients, ArrayList<Seasoning> seasonings) {
        this.type = type;
        this.ingredients = ingredients;
        this.tool = tool;
        this.seasonings = seasonings;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        type = RecipeType.values()[nbt.getInteger("RecipeType")];
        tool = Utensil.values()[nbt.getInteger("ToolType")];
        
        // Ingredients
        NBTTagList ingredient = nbt.getTagList("IngredientsList", 10);
        for (int i = 0; i < ingredient.tagCount(); i++) {
            NBTTagCompound tag = ingredient.getCompoundTagAt(i);
            String name = tag.getString("ItemName");
            int damage = tag.getInteger("ItemDamage");
            SafeStack stack = new SafeStack(name, damage);
            ingredients.add(FoodRegistry.getIngredient(stack));
        }

        // Ingredients
        NBTTagList seasoning = nbt.getTagList("SeasoningsList", 10);
        for (int i = 0; i < seasoning.tagCount(); i++) {
            NBTTagCompound tag = seasoning.getCompoundTagAt(i);
            String name = tag.getString("ItemName");
            int damage = tag.getInteger("ItemDamage");
            SafeStack stack = new SafeStack(name, damage);
            seasonings.add(FoodRegistry.getSeasoning(stack));
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("RecipeType", type.ordinal());
        nbt.setInteger("ToolType", tool.ordinal());
        
        // Ingredients
        NBTTagList ingredient = new NBTTagList();
        for (Ingredient i : ingredients) {
            SafeStack key = i.keys.iterator().next();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ItemName", key.item);
            tag.setInteger("ItemDamage", key.damage);
            ingredient.appendTag(tag);
        }

        nbt.setTag("IngredientsList", ingredient);

        // Ingredients
        NBTTagList seasoning = new NBTTagList();
        for (Seasoning i : seasonings) {
            SafeStack key = i.keys.iterator().next();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("ItemName", key.item);
            tag.setInteger("ItemDamage", key.damage);
            seasoning.appendTag(tag);
        }

        nbt.setTag("SeasoningsList", seasoning);
    }
}
