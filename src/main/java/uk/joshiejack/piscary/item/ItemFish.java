package uk.joshiejack.piscary.item;

import uk.joshiejack.penguinlib.item.base.ItemMultiEdible;
import uk.joshiejack.penguinlib.item.interfaces.Edible;
import uk.joshiejack.piscary.Piscary;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

import static uk.joshiejack.piscary.Piscary.MODID;

public class ItemFish extends ItemMultiEdible<ItemFish.Fish> {
    public ItemFish() {
        super(new ResourceLocation(MODID, "fish"), Fish.class);
        setCreativeTab(Piscary.TAB);
    }

    public enum Fish implements IStringSerializable, Edible {
        ANCHOVY(1, 0.1F), ANGEL(1, 0.1F), ANGLER(1, 0.1F), BASS(1, 0.1F),
        BLAASOP(1, 0.1F), BOWFIN(1, 0.1F), BUTTERFLY(1, 0.1F), CARP(1, 0.1F),
        CATFISH(1, 0.1F), CHUB(1, 0.1F), CLOWN(1, 0.1F), COD(1, 0.1F),
        DAMSEL(1, 0.1F), ELECTRICRAY(1, 0.1F), GOLD(1, 0.1F), HERRING(1, 0.1F),
        KOI(1, 0.1F), LAMPREY(1, 0.1F), LUNGFISH(1, 0.1F), MANTARAY(1, 0.1F),
        MINNOW(1, 0.1F), PERCH(1, 0.1F), PICKEREL(1, 0.1F), PIKE(1, 0.1F),
        PIRANHA(1, 0.1F), PUFFER(1, 0.1F), PUPFISH(1, 0.1F), SALMON(1, 0.1F),
        SARDINE(1, 0.1F), SIAMESE(1, 0.1F), STARGAZER(1, 0.1F), STINGRAY(1, 0.1F),
        TANG(1, 0.1F), TETRA(1, 0.1F), TROUT(1, 0.1F), TUNA(1, 0.1F), WALLEYE(1, 0.1F);
        private final int hunger;
        private final float saturation;

        Fish (int hunger, float saturation) {
            this.hunger = hunger;
            this.saturation = saturation;
        }

        public boolean isPoisonous() {
            return this == BLAASOP || this == LAMPREY || this == PUFFER || this == STARGAZER || this == STINGRAY;
        }

        public boolean isTropical() {
            return isPoisonous() || this == TETRA || this == CLOWN || this == BUTTERFLY || this == ANGEL || this == SIAMESE || this == TANG;
        }

        public boolean isEdible() {
            return !isPoisonous() && !isTropical();
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
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
