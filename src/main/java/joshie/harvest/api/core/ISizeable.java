package joshie.harvest.api.core;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

/** Items that implement this, come in small, medium and large **/
public interface ISizeable {
    long getValue(Size size);

    enum Size implements IStringSerializable {
        SMALL(0), MEDIUM(20000), LARGE(40000);

        private final int relationship;

        Size(int relationship) {
            this.relationship = relationship;
        }

        public int getRelationshipRequirement() {
            return relationship;
        }

        @Override
        public String getName() {
            return name().toLowerCase(Locale.US);
        }
    }
}