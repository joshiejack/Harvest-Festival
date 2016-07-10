package joshie.harvest.cooking.items;

import joshie.harvest.cooking.items.ItemIngredients.Ingredient;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.ItemHFEnum;
import net.minecraft.util.IStringSerializable;

public class ItemIngredients extends ItemHFEnum<ItemIngredients, Ingredient> {
    public enum Ingredient implements IStringSerializable {
        BUTTER(false), KETCHUP(false), COOKIES(false), EGG_SCRAMBLED(false), SASHIMI(false),
        FLOUR, OIL, RICEBALL, SALT, CHOCOLATE;

        private final boolean isReal;

        private Ingredient() {
            isReal = true;
        }

        private Ingredient(boolean isReal) {
            this.isReal = isReal;
        }

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public ItemIngredients() {
        super(HFTab.COOKING, Ingredient.class);
    }

    @Override
    public boolean shouldDisplayInCreative(Ingredient ingredient) {
        return ingredient.isReal;
    }
}
