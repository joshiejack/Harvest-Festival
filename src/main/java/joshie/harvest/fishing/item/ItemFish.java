package joshie.harvest.fishing.item;

import joshie.harvest.api.core.IShippable;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.item.ItemHFSizeable;
import joshie.harvest.fishing.item.ItemFish.Fish;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class ItemFish extends ItemHFSizeable<ItemFish, Fish> implements IShippable {
    public ItemFish() {
        super(HFTab.FISHING, Fish.class);
    }

    public enum Fish implements IStringSerializable, joshie.harvest.core.util.interfaces.ISizeable {
        ANGEL, ANGLER, BLAASOP, BOWFIN, BUTTERFLY, CATFISH, CHUB, CLOWN, COD, DAMSEL, ELECTRICRAY, GOLD, HERRING, KOI, LAMPREY,
        LUNGFISH, MANTARAY, MINNOW, PERCH, PICKEREL, PIRANHA, PUFFER, PUPFISH, SALMON, SIAMESE, STARGAZER, STINGRAY, TANG, TETRA, TROUT, TUNA;

        private final long small;
        private final long medium;
        private final long large;

        Fish() {
            this.small = 0;
            this.medium = 0;
            this.large = 0;
        }

        Fish(long small, long medium, long large) {
            this.small = small;
            this.medium = medium;
            this.large = large;
        }

        @Override
        public long getSmall() {
            return small;
        }

        @Override
        public long getMedium() {
            return medium;
        }

        @Override
        public long getLarge() {
            return large;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }

        @Override
        public int getMeta() {
            return ordinal();
        }
    }
}
