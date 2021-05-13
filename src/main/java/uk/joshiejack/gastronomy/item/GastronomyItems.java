package uk.joshiejack.gastronomy.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;

import java.util.Locale;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(MODID)
@Mod.EventBusSubscriber(modid = MODID)
public class GastronomyItems {
    public static final ItemIngredient INGREDIENT = null;
    public static final ItemFood FOOD = null;
    public static final ItemDrink DRINK = null;
    public static final ItemBurnt BURNT = null;
    public static final ItemCookbook COOKBOOK = null;
    public static final ItemUtensil UTENSIL = null;
    public static final ItemRecipe RECIPE = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemIngredient(), new ItemFood(), new ItemDrink(), new ItemBurnt(),
                                        new ItemCookbook(), new ItemUtensil(), new ItemRecipe());
    }

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        OreDictionary.registerOre("foodFlour", INGREDIENT.getStackFromEnum(ItemIngredient.Ingredient.FLOUR));
        OreDictionary.registerOre("foodCurrypowder", INGREDIENT.getStackFromEnum(ItemIngredient.Ingredient.CURRY_POWDER));
        for (ItemFood.Food food: ItemFood.Food.values()) {
            StackHelper.registerSynonym(food.name().toLowerCase(Locale.ENGLISH), FOOD, food.ordinal());
        }

        for (ItemDrink.Drink drink: ItemDrink.Drink.values()) {
            StackHelper.registerSynonym(drink.name().toLowerCase(Locale.ENGLISH), DRINK, drink.ordinal());
        }

        for (ItemIngredient.Ingredient ingredient: ItemIngredient.Ingredient.values()) {
            StackHelper.registerSynonym(ingredient.name().toLowerCase(Locale.ENGLISH), INGREDIENT, ingredient.ordinal());
        }
    }
}
