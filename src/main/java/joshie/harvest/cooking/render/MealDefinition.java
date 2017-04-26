package joshie.harvest.cooking.render;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.cooking.item.ItemMeal;
import joshie.harvest.cooking.item.ItemMeal.Meal;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import static joshie.harvest.cooking.tile.TileCooking.IN_UTENSIL;

public class MealDefinition implements ItemMeshDefinition {
    private final TIntObjectMap<ModelResourceLocation> alts = new TIntObjectHashMap<>();
    private final TIntObjectMap<ModelResourceLocation> meals = new TIntObjectHashMap<>();

    public MealDefinition() {
        Item item = HFCooking.MEAL;
        //Register the normal meals
        for (Meal meal: Meal.values()) {
            ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), meal.getName());
            ModelBakery.registerItemVariants(item, model);
            meals.put(meal.ordinal(), model);

            //Check for alts
            if (meal.hasAltTexture()) {
                ModelResourceLocation altModel = new ModelResourceLocation(item.getRegistryName(), meal.getName() + "_alt");
                ModelBakery.registerItemVariants(item, altModel);
                alts.put(meal.ordinal(), altModel);
            }
        }
    }

    private int getMealMetaFromStack(@Nonnull ItemStack stack) {
        if (stack.getItemDamage() >= 0 && stack.getItemDamage() < ItemMeal.MEALS.length) {
            return stack.getItemDamage();
        }

        return 0;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    @Nonnull
    public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
        if (stack.hasTagCompound()) {
            Meal meal = HFCooking.MEAL.getEnumFromStack(stack);
            if (meal.hasAltTexture()) {
                return stack.getTagCompound().hasKey(IN_UTENSIL) ? alts.get(getMealMetaFromStack(stack)) : meals.get(getMealMetaFromStack(stack));
            }
        }

        return meals.get(getMealMetaFromStack(stack));
    }
}
