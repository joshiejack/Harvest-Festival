package joshie.harvestmoon.init;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class HMRecipeFixes {
    public static void init() {
        fixRecipes(Items.egg);
        fixRecipes(Items.carrot);
        fixRecipes(Items.potato);
    }

    public static void fixRecipes(Item fix) {
        for (IRecipe recipe : (ArrayList<IRecipe>) CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof ShapedRecipes) {
                overrideShaped((ShapedRecipes) recipe, fix);
            } else if (recipe instanceof ShapelessRecipes) {
                overrideShapeless((ShapelessRecipes) recipe, fix);
            } else if (recipe instanceof ShapedOreRecipe) {
                overrideShapedOre((ShapedOreRecipe) recipe, fix);
            }
        }
    }

    private static void overrideShapedOre(ShapedOreRecipe recipe, Item fix) {
        Object[] original = recipe.getInput();
        Object[] clone = new Object[original.length];
        for (int i = 0; i < original.length; i++) {
            boolean hasChanged = false;
            Object o = original[i];
            if (o == null || !(o instanceof ItemStack)) {
                clone[i] = o;
                continue;
            }
            
            ItemStack stack = (ItemStack) o;
            Item item = stack.getItem();
            if (item == fix) {
                clone[i] = new ItemStack(fix, 1, OreDictionary.WILDCARD_VALUE);
                ((ItemStack) clone[i]).setTagCompound(stack.getTagCompound());
                hasChanged = true;
            } else clone[i] = ((ItemStack) original[i]).copy();

            if (hasChanged) {
                override(recipe, clone);
            }
        }
    }

    private static void overrideShapeless(ShapelessRecipes recipe, Item fix) {
        ArrayList<ItemStack> original = (ArrayList<ItemStack>) recipe.recipeItems;
        ArrayList<ItemStack> clone = new ArrayList(original.size());
        while (clone.size() < original.size()) clone.add(null);
        for (int i = 0; i < original.size(); i++) {
            boolean hasChanged = false;
            ItemStack stack = original.get(i);
            if (stack == null) {
                clone.set(i, null);
                continue;
            }
            
            Item item = stack.getItem();
            if (item == fix) {
                ItemStack stack2 = new ItemStack(fix, 1, OreDictionary.WILDCARD_VALUE);
                stack2.setTagCompound(stack.getTagCompound());
                clone.set(i, stack2);
                hasChanged = true;
            } else clone.set(i, stack);

            if (hasChanged) {
                override(recipe, clone);
            }
        }
    }

    private static void overrideShaped(ShapedRecipes shaped, Item fix) {
        ItemStack[] original = shaped.recipeItems;
        ItemStack[] clone = new ItemStack[original.length];
        for (int i = 0; i < original.length; i++) {
            boolean hasChanged = false;
            ItemStack stack = original[i];
            if (stack == null) {
                clone[i] = null;
                continue;
            }
            
            Item item = stack.getItem();
            if (item == fix) {
                clone[i] = new ItemStack(fix, 1, OreDictionary.WILDCARD_VALUE);
                clone[i].setTagCompound(stack.getTagCompound());
                hasChanged = true;
            } else clone[i] = original[i].copy();

            if (hasChanged) {
                override(shaped, clone);
            }
        }
    }

    private static void override(Object recipe, Object items) {
        try {
            Field field = recipe.getClass().getDeclaredField("recipeItems");
            if (field == null) field = recipe.getClass().getDeclaredField("field_77574_d");
            if (field == null) field = recipe.getClass().getDeclaredField("field_77579_b");
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(recipe, items);
        } catch (Exception e) {}
    }
}
