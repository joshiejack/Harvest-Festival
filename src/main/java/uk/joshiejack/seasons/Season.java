package uk.joshiejack.seasons;

import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;

import java.util.Locale;

public enum Season {
    SPRING, SUMMER, AUTUMN, WINTER, WET, DRY;

    public static final Season[] MAIN = new Season[4];

    //Add the four main seasons
    static {
        for (int i = 0; i < 4; i++) {
            MAIN[i] = values()[i];
        }
    }

    public static Season fromTime(long time) {
        return MAIN[Math.max(0, (int)Math.floor(((float) TimeHelper.getElapsedDays(time) / (float) (SeasonsConfig.daysPerSeasonMultiplier * Seasons.DAYS_PER_SEASON)) % 4))];
    }

    public String getUnlocalizedName() {
        return Seasons.MODID + "." + name().toLowerCase(Locale.ENGLISH);
    }

    public String getName() {
        return StringHelper.localize(getUnlocalizedName());
    }
}
