package uk.joshiejack.horticulture.item;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import net.minecraft.util.ResourceLocation;


public class ItemFood extends ItemMultiEdible<ItemFood.Meal> {
    public ItemFood() {
        super(new ResourceLocation(Horticulture.MODID, "meal"), Meal.class);
        setCreativeTab(Horticulture.TAB);
    }

    public enum Meal implements Edible {
        CORNFLAKES(6, 0.6F), HAPPY_EGGPLANT(4, 0.6F), BAKED_CORN(3, 0.4F),
        PICKLED_TURNIP(5, 0.5F), PICKLED_CUCUMBER(4, 0.4F), SALAD(7, 0.8F),
        BOILED_SPINACH(3, 0.6F), CANDIED_POTATO(4, 0.6F), PUMPKIN_STEW(5, 0.2F),
        STIR_FRY(6, 0.4F);

        private final int hunger;
        private final float saturation;

        Meal(int hunger, float saturation) {
            this.hunger = hunger;
            this.saturation = saturation;
        }

        @Override
        public int getHunger() {
            return hunger;
        }

        @Override
        public float getSaturation() {
            return saturation;
        }
    }
}
