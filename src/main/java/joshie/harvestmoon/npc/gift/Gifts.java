package joshie.harvestmoon.npc.gift;

import net.minecraft.item.ItemStack;

public abstract class Gifts {
    public GiftQuality getValue(ItemStack stack) {
        return GiftQuality.DECENT;
    }

    public static enum GiftQuality {
        AWESOME(800), GOOD(500), DECENT(300), DISLIKE(-500), BAD(-800), TERRIBLE(-5000);

        private final int relationpoints;

        private GiftQuality(int relationpoints) {
            this.relationpoints = relationpoints;
        }

        public int getRelationPoints() {
            return relationpoints;
        }
    }
}
