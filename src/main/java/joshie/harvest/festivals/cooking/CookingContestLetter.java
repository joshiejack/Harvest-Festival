package joshie.harvest.festivals.cooking;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.Letter;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class CookingContestLetter extends Letter {
    public CookingContestLetter() {
        super(new ResourceLocation(MODID, "cooking"));
        setTownLetter(); //Mark this as a town based letter
    }

    @Override
    public boolean isExpired(CalendarDate today, int days) {
        return expires() && days >= getExpiry() || today.getSeason() != Season.SPRING;
    }
}
