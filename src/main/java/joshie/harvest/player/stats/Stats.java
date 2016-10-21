package joshie.harvest.player.stats;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.player.IPlayerStats;

import static joshie.harvest.api.calendar.Season.WINTER;

public abstract class Stats implements IPlayerStats {
    protected CalendarDate birthday = new CalendarDate(0, WINTER, 0);
    protected long gold;

    public CalendarDate getBirthday() {
        return birthday;
    }

    @Override
    public long getGold() {
        return gold;
    }
}
