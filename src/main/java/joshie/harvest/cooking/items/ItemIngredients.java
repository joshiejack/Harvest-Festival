package joshie.harvest.cooking.items;

import joshie.harvest.cooking.items.ItemIngredients.Ingredient;
import joshie.harvest.core.util.base.ItemHFEnum;
import net.minecraft.util.IStringSerializable;

public class ItemIngredients extends ItemHFEnum<ItemIngredients, Ingredient> {
    public enum Ingredient implements IStringSerializable {
        BUTTER, KETCHUP, COOKIES, EGG_SCRAMBLED, SASHIMI;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public ItemIngredients() {
        super(Ingredient.class);
    }
}
