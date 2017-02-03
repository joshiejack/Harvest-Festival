package joshie.harvest.festivals.cooking;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.festivals.LetterFestival;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;
import static joshie.harvest.festivals.HFFestivals.COOKING_FESTIVAL;

public class CookingContestLetter extends LetterFestival {
    public CookingContestLetter() {
        super(COOKING_FESTIVAL, new ResourceLocation(MODID, "cooking"));
    }

    @Override
    public boolean isExpired(CalendarDate today, int days) {
        return expires() && days >= getExpiry() || today.getSeason() != Season.SPRING;
    }
}
