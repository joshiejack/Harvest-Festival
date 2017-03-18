package joshie.harvest.quests.town.festivals;

public enum Place {
    FIRST(2500), SECOND(1000), THIRD(250);

    public static final Place VALUES[] = new Place[] { FIRST, SECOND, THIRD};

    public final int happiness;

    Place(int happiness) {
        this.happiness = happiness;
    }
}
