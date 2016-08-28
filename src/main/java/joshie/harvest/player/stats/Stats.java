package joshie.harvest.player.stats;

import joshie.harvest.api.calendar.CalendarDate;

import static joshie.harvest.api.calendar.Season.WINTER;

public abstract class Stats {
    protected CalendarDate birthday = new CalendarDate(0, WINTER, 0);
    protected long gold;

    public CalendarDate getBirthday() {
        return birthday;
    }

    public long getGold() {
        return gold;
    }
}
