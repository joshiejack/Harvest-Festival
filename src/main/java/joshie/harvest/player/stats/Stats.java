package joshie.harvest.player.stats;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;

import static joshie.harvest.api.calendar.Season.NETHER;

public abstract class Stats {
    protected ICalendarDate birthday = HFApi.calendar.newDate(0, NETHER, 0);
    protected long gold;

    public ICalendarDate getBirthday() {
        return birthday;
    }

    public long getGold() {
        return gold;
    }
}
