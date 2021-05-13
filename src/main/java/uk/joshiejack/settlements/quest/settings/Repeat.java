package uk.joshiejack.settlements.quest.settings;

public enum Repeat {
    NONE(-1), ALWAYS(0), DAILY(1), WEEKLY(7), SEASONLY(30), YEARLY(365);

    private final int days;

    Repeat(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
