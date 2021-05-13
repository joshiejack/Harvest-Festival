package uk.joshiejack.horticulture.item;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;

import javax.annotation.Nonnull;
import java.util.Locale;


public class ItemCrop extends ItemMultiEdible<ItemCrop.Crops> {
    public ItemCrop() {
        super(new ResourceLocation(Horticulture.MODID, "crop"), Crops.class);
        setCreativeTab(Horticulture.TAB);
    }

    public enum SeedType {
        SEED, TRELLIS, SAPLING
    }

    public enum Crops implements IStringSerializable, Edible {
        TURNIP(2, 0.4F), CUCUMBER(2, 0.25F), STRAWBERRY(2, 0.6F), CABBAGE(1, 0.5F),
        ONION(1, 0.4F), TOMATO(2, 0.5F), CORN(1, 0.3F), PINEAPPLE(2, 0.6F),
        EGGPLANT(1, 0.5F), SPINACH(1, 0.6F), SWEET_POTATO(1, 0.35F), GREEN_PEPPER(1, 0.5F),
        BANANA(4, 0.4F, SeedType.SAPLING), GRAPE(3, 0.5F, SeedType.TRELLIS),
        ORANGE(3, 0.3F, SeedType.SAPLING), PEACH(3, 0.4F, SeedType.SAPLING);

        private final int hunger;
        private final float saturation;
        private final SeedType type;

        Crops(int hunger, float saturation) {
            this.hunger = hunger;
            this.saturation = saturation;
            this.type = SeedType.SEED;
        }

        Crops(int hunger, float saturation, SeedType type) {
            this.hunger = hunger;
            this.saturation = saturation;
            this.type = type;
        }

        public SeedType getType() {
            return type;
        }

        @Override
        public int getHunger() {
            return hunger;
        }

        @Override
        public float getSaturation() {
            return saturation;
        }

        @Nonnull
        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
