package joshie.harvest.quests.town.festivals;

public enum Place {
    FIRST(1000), SECOND(500), THIRD(250);

    public static final Place VALUES[] = new Place[] { FIRST, SECOND, THIRD};

    public final int happiness;

    Place(int happiness) {
        this.happiness = happiness;
    }
}
