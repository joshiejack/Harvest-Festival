package joshie.harvest.cooking.render;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.cooking.recipe.Recipe;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.core.base.FMLDefinition;
import joshie.harvest.core.base.ItemHFFML;
import joshie.harvest.core.helpers.ModelHelper;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;

public class MealDefinition extends FMLDefinition<Recipe> {
    private TIntObjectMap<ModelResourceLocation> burnt = new TIntObjectHashMap<>();

    public MealDefinition(ItemHFFML item, String name, FMLControlledNamespacedRegistry<Recipe> registry) {
        super(item, name, registry);
    }

    public void registerBurnt(int damage, ModelResourceLocation resource) {
        burnt.put(damage, resource);
    }

    public int getMetaFromStack(ItemStack stack) {
        if (stack.getItemDamage() >= 0 && stack.getItemDamage() < Utensil.values().length) {
            return stack.getItemDamage();
        }

        return 0;
    }

    @Override
    public void registerEverything() {
        super.registerEverything();
        //Register the burnt meals
        for (Utensil utensil: Utensil.values()) {
            ModelResourceLocation model = ModelHelper.getModelForItem("meals/burnt" + utensil.name());
            ModelBakery.registerItemVariants(item, model);
            registerBurnt(utensil.ordinal(), model);
        }
    }

    @Override
    public ModelResourceLocation getModelLocation(ItemStack stack) {
        if (stack.hasTagCompound()) {
            return super.getModelLocation(stack);
        }

        return burnt.get(getMetaFromStack(stack));
    }
}
