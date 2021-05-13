package uk.joshiejack.husbandry.item;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import net.minecraft.util.ResourceLocation;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class ItemFood extends ItemMultiEdible<ItemFood.Food> {
    public ItemFood() {
        super(new ResourceLocation(MODID, "food"), Food.class);
        setCreativeTab(Husbandry.TAB);
    }

    public enum Food implements Edible {
        SMALL_MAYONNAISE(3, 0.6F), MEDIUM_MAYONNAISE(4, 0.8F), LARGE_MAYONNAISE(5, 1F),
        BUTTER(1, 0.2F), /*//TODO: food values >>>*/DINNERROLL(1, 0.1F), BOILED_EGG(1, 0.1F),
        ICE_CREAM(1, 0.1F), SMALL_CHEESE(1, 0.1F), MEDIUM_CHEESE(1, 0.1F), LARGE_CHEESE(1, 0.1F),
        SMALL_EGG(1, 0.1F), MEDIUM_EGG(1, 0.1F), LARGE_EGG(1, 0.1F), HONEY(1, 0.1F),
        SMALL_DUCK_EGG(1, 0.1F), MEDIUM_DUCK_EGG(1, 0.1F), LARGE_DUCK_EGG(1, 0.1F),
        SMALL_DUCK_MAYONNAISE(3, 0.6F), MEDIUM_DUCK_MAYONNAISE(4, 0.8F), LARGE_DUCK_MAYONNAISE(5, 1F);

        private final int hunger;
        private final float saturation;

        Food(int hunger, float saturation) {
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
