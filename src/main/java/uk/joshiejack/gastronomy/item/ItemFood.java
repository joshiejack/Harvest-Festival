package uk.joshiejack.gastronomy.item;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import net.minecraft.util.ResourceLocation;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class ItemFood extends ItemMultiEdible<ItemFood.Food> {
    public ItemFood() {
        super(new ResourceLocation(MODID, "food"), Food.class);
        setCreativeTab(Gastronomy.TAB);
    }

    public enum Food implements Edible {
        RICEBALL(1, 0.25F), CHOCOLATE(3, 0.5F),
        CURRY_BREAD(6, 0.6F), RAISIN_BREAD(6, 0.7F), CAKE(8, 0.2F), CHOCOLATE_CAKE(10, 0.1F),
        COOKIES(1, 0.1F), CHOCOLATE_COOKIES(1, 0.1F), CURRY_RICE(1, 0.1F), DORIA(1, 0.1F),
        DOUGHNUT(1, 0.1F), DUMPLINGS(1, 0.1F), EGG_OVERRICE(1, 0.1F),
        SCRAMBLED_EGG(1, 0.1F), GRILLED_FISH(1, 0.1F), FRIES_FRENCH(1, 0.1F),
        NOODLES(1, 0.1F), CURRY_NOODLES(1, 0.1F),
        TEMPURA_NOODLES(1, 0.1F), THICK_FRIED_NOODLES(1, 0.1F), OMELET(1, 0.1F),
        OMELET_RICE(1, 0.1F), PANCAKE(1, 0.1F), SAVOURY_PANCAKE(1, 0.1F),
        APPLE_PIE(1, 0.1F), POPCORN(1, 0.1F), PORRIDGE(1, 0.1F),
        POTSTICKER(1, 0.1F), BAMBOO_RICE(1, 0.1F), FRIED_RICE(1, 0.1F),
        MATSUTAKE_RICE(1, 0.1F), MUSHROOM_RICE(1, 0.1F), TEMPURA_RICE(1, 0.1F),
        TOASTED_RICEBALLS(1, 0.1F), RISOTTO(1, 0.1F), HERB_SALAD(1, 0.1F),
        SANDWICH(1, 0.1F), FRUIT_SANDWICH(1, 0.1F), HERB_SANDWICH(1, 0.1F),
        CHIRASHI_SUSHI(1, 0.1F), APPLE_SOUFFLE(1, 0.1F),  HERB_SOUP(1, 0.1F),
        RICE_SOUP(1, 0.1F), STEW(1, 0.1F), MOUNTAIN_STEW(1, 0.1F),
        SUSHI(1, 0.1F), SWEET_POTATOES(1, 0.1F), TEMPURA(1, 0.1F),
        TOAST(1, 0.1F), FRENCH_TOAST(1, 0.1F);


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
