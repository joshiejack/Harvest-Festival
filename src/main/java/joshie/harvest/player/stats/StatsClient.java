package joshie.harvest.player.stats;

import joshie.harvest.api.calendar.CalendarDate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StatsClient extends Stats {
    public void setBirthday(CalendarDate birthday) {
        this.birthday = birthday;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }
}
