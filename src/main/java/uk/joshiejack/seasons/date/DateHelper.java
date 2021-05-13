package uk.joshiejack.seasons.date;

import net.minecraft.world.World;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import uk.joshiejack.seasons.Seasons;

public class DateHelper {
    public static int getYear(long time) {
        //TODO: FIX return (int) Math.floor((double) getElapsedDays(time) / 4 / SeasonsConfig.daysPerSeason);
        return (int) Math.floor((double) TimeHelper.getElapsedDays(time) / 4 / Seasons.DAYS_PER_SEASON);
    }

    public static int getDay(long totalTime) {
        return TimeHelper.getElapsedDays(totalTime) % Seasons.DAYS_PER_SEASON;
    }

    public static CalendarDate getDate(World world) {
        return DateWorldData.get(world);
    }
}
