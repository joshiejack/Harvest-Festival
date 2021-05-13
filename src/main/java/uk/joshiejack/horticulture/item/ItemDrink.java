package uk.joshiejack.horticulture.item;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import net.minecraft.item.EnumAction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Locale;


public class ItemDrink extends ItemMultiEdible<ItemDrink.Drink> {
    public ItemDrink() {
        super(new ResourceLocation(Horticulture.MODID, "drink"), ItemDrink.Drink.class);
        setCreativeTab(Horticulture.TAB);
    }

    public enum Drink implements IStringSerializable, Edible {
        PINEAPPLE_JUICE(3, 0.4F), TOMATO_JUICE(3, 0.3F), STRAWBERRY_MILK(4, 0.5F),
        GRAPE_JUICE(4, 0.4F), PEACH_JUICE(4, 0.3F), BANANA_JUICE(5, 0.3F),
        ORANGE_JUICE(5, 0.2F), APPLE_JUICE(4, 0.2F);

        private final int hunger;
        private final float saturation;

        Drink(int hunger, float saturation) {
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

        @Override
        public EnumAction getAction() {
            return EnumAction.DRINK;
        }

        @Override
        public @Nonnull String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
