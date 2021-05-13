package uk.joshiejack.husbandry.item;

import uk.joshiejack.husbandry.Husbandry;
import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import net.minecraft.item.EnumAction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Locale;

import static uk.joshiejack.husbandry.Husbandry.MODID;

public class ItemDrink extends ItemMultiEdible<ItemDrink.Drink> {
    public ItemDrink() {
        super(new ResourceLocation(MODID, "drink"), Drink.class);
        setCreativeTab(Husbandry.TAB);
    }

    @SuppressWarnings("SameParameterValue")
    public enum Drink implements IStringSerializable, Edible {
        HOT_MILK(1, 0.1F), SMALL_MILK(1, 0.1F), MEDIUM_MILK(1, 0.1F), LARGE_MILK(1, 0.1F),
        SMALL_TRUFFLE_OIL(1, 0.1F), MEDIUM_TRUFFLE_OIL(1, 0.1F), LARGE_TRUFFLE_OIL(1, 0.1F); //TODO: Food values

        private final int hunger;
        private final float saturation;

        Drink(int hunger, float saturation) {
            this.hunger = hunger;
            this.saturation = saturation;
        }

        @Override
        public EnumAction getAction() {
            return EnumAction.DRINK;
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
        public @Nonnull String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
 }
