package uk.joshiejack.penguinlib.util.helpers.minecraft;

import uk.joshiejack.penguinlib.PenguinConfig;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.ItemDinnerware;
import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Locale;

import static uk.joshiejack.penguinlib.item.PenguinItems.DINNERWARE;
import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;
import static uk.joshiejack.penguinlib.item.ItemDinnerware.Dinnerware.*;

public class RecipeHelper {
    private static final ResourceLocation GLASS_CONTAINER = new ResourceLocation(MOD_ID, "glass");
    private static final ResourceLocation PICKLING_JAR = new ResourceLocation(MOD_ID, "pickling_jar");
    private static final ResourceLocation BOWL_CONTAINER = new ResourceLocation(MOD_ID, "bowl");
    private static final ResourceLocation PLATE_CONTAINER = new ResourceLocation(MOD_ID, "plate");
    private final IForgeRegistry<IRecipe> registry;
    private final String modid;
    public RecipeHelper(IForgeRegistry<IRecipe> registry, String modid) {
        this.registry = registry;
        this.modid = modid;
    }

    private <E extends Enum<E> & Edible> void shapelessMealRecipe(ItemMultiEdible<E> edible, ItemDinnerware.Dinnerware dinnerware, ResourceLocation group, E meal, Object... fruit) {
        PenguinConfig.forceDinnerwareItem = true;
        if (PenguinConfig.requireDishes) {
            shapelessRecipe(meal.name().toLowerCase(Locale.ENGLISH), group, edible.getStackFromEnum(meal), getList(fruit, DINNERWARE.getStackFromEnum(dinnerware)));
            if (PenguinConfig.returnDishes) edible.registerContainer(meal, dinnerware);
        } else shapelessRecipe(meal.name().toLowerCase(Locale.ENGLISH), null, DINNERWARE.getStackFromEnum(dinnerware), fruit);
    }

    private static Object[] getList(Object[] ingredients, Object container) {
        Object[] list = new Object[ingredients.length + 1];
        list[0] = container; //Make the first entry a bowl
        System.arraycopy(ingredients, 0, list, 1, ingredients.length);
        return list;
    }

    public <E extends Enum<E> & Edible> void glassRecipe(ItemMultiEdible<E> edible, E meal, Object... fruit) {
        shapelessMealRecipe(edible, GLASS, GLASS_CONTAINER, meal, fruit);
        PenguinLib.addGlass = true;
    }

    public <E extends Enum<E> & Edible> void picklingJarRecipe(ItemMultiEdible<E> edible, E meal, Object... fruit) {
        shapelessMealRecipe(edible, ItemDinnerware.Dinnerware.PICKLING_JAR, PICKLING_JAR, meal, fruit);
        PenguinLib.addPicklingJar = true;
    }

    public <E extends Enum<E> & Edible> void bowlRecipe(ItemMultiEdible<E> edible, E meal, Object... fruit) {
        shapelessMealRecipe(edible, BOWL, BOWL_CONTAINER, meal, fruit);
        PenguinLib.addBowl = true;
    }

    public <E extends Enum<E> & Edible> void plateRecipe(ItemMultiEdible<E> edible , E meal, Object... fruit) {
        shapelessMealRecipe(edible, PLATE_FIRED, PLATE_CONTAINER, meal, fruit);
        PenguinLib.addPlate = true;
    }

    public void shapelessRecipe(String name, ItemStack stack, Object... objects) {
        shapelessRecipe(name, null, stack, objects);
    }


    public void shapelessRecipe(String name, ResourceLocation group, ItemStack stack, Object... objects) {
        registry.register(new ShapelessOreRecipe(group, stack, objects).setRegistryName(new ResourceLocation(modid, name)));
    }

    public void shapedRecipe(String name, ItemStack stack, Object... objects) {
        shapedRecipe(name, null, stack, objects);
    }

    public void shapedRecipe(String name, ResourceLocation group, ItemStack stack, Object... objects) {
        registry.register(new ShapedOreRecipe(group, stack, objects).setRegistryName(new ResourceLocation(modid, name)));
    }
}
