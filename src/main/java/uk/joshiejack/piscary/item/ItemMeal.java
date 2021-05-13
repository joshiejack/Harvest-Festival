package uk.joshiejack.piscary.item;

import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import uk.joshiejack.piscary.Piscary;
import net.minecraft.util.ResourceLocation;

import static uk.joshiejack.piscary.Piscary.MODID;

public class ItemMeal extends ItemMultiEdible<ItemMeal.Meal> {
    public ItemMeal() {
        super(new ResourceLocation(MODID, "meal"), Meal.class);
        setCreativeTab(Piscary.TAB);
    }

    public enum Meal implements Edible {
        FISHSTICKS(1, 0.1F), SASHIMI(1, 0.1F), FISH_STEW(1, 0.1F);
        private final int hunger;
        private final float saturation;

        Meal (int hunger, float saturation) {
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
