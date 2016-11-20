package joshie.harvest.api.player;

public enum RelationshipType {
    ANIMAL(27000), NPC(50000);

    private final int maximum;
    RelationshipType(int maximum) {
        this.maximum = maximum;
    }

    public int getMaximumRP() {
        return maximum;
    }
}
