package joshie.harvest.player.stats;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import net.minecraft.world.World;

public class StatData {
    protected ICalendarDate birthday = HFApi.calendar.newDate(0, null, 0);
    protected long gold;

    public ICalendarDate getBirthday() {
        return birthday;
    }

    public boolean isBirthdaySet() {
        return birthday.getSeason() != null && birthday.getDay() != 0 && birthday.getYear() != 0;
    }

    public boolean setBirthday(World world) {
        if (!isBirthdaySet()) {
            birthday = HFApi.calendar.cloneDate(HFApi.calendar.getDate(world));
            return true;
        } else return false;
    }
    
    public void setBirthday(ICalendarDate newDate) {
        birthday = newDate;
    }

    public long getGold() {
        return gold;
    }
}
