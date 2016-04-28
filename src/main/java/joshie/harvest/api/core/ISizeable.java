package joshie.harvest.api.core;

import net.minecraft.item.ItemStack;

/** Items that implement this, come in small, medium and large **/
public interface ISizeable {
    public Size getSize(ItemStack stack);

    public static enum Size {
        SMALL(0), MEDIUM(20000), LARGE(40000);

        private final int relationship;

        private Size(int relationship) {
            this.relationship = relationship;
        }

        public int getRelationshipRequirement() {
            return relationship;
        }
    }
}