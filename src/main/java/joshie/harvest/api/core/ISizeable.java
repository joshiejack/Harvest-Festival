package joshie.harvest.api.core;

/** Items that implement this, come in small, medium and large **/
public interface ISizeable {
    long getValue(Size size);

    public enum Size {
        SMALL(0), MEDIUM(20000), LARGE(40000);

        private final int relationship;

        Size(int relationship) {
            this.relationship = relationship;
        }

        public int getRelationshipRequirement() {
            return relationship;
        }
    }
}