package joshie.harvest.calendar;

import joshie.harvest.api.calendar.ICalendar;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.HFApiImplementation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumMap;

import static net.minecraft.util.text.TextFormatting.*;

@HFApiImplementation
public class CalendarAPI implements ICalendar {
    public static final CalendarAPI INSTANCE = new CalendarAPI();
    private final EnumMap<Season, SeasonData> data = new EnumMap<>(Season.class);

    private CalendarAPI() {
        data.put(Season.SPRING, new SeasonData(Season.SPRING, 0x87CEFA, 0.65D, 0.0F, GREEN, 0x00D900, 80D, 100D, 0D, 0D, 0D));
        data.put(Season.SUMMER, new SeasonData(Season.SUMMER, 7972863, 0.4D, 0.0011F, YELLOW, 0xFFFF4D, 95D, 75D, 100D, 0D, 0D));
        data.put(Season.AUTUMN, new SeasonData(Season.AUTUMN, 0x8CBED6, 1.08D, -0.07F, GOLD, 0x8C4600, 50D, 100D, 0D, 0D, 0D));
        data.put(Season.WINTER, new SeasonData(Season.WINTER, 0xFFFFFF, 1.56D, -0.1375F, BLUE, 0xFFFFFF, 45D, 0D, 0D, 90D, 100D));

        //TODO: Give these real values
        data.put(Season.NETHER, new SeasonData(Season.NETHER, 7972863, 0.4D, 0.0011F, RED, 0xFFFF4D, 95D, 75D, 100D, 0D, 0D));
        data.put(Season.END, new SeasonData(Season.END, 7972863, 0.4D, 0.0011F, GRAY, 0xFFFF4D, 95D, 75D, 100D, 0D, 0D));
    }

    @Override
    public ICalendarDate getDate(World world) {
        return HFTrackers.getCalendar(world).getDate();
    }

    @Override
    public ICalendarDate cloneDate(ICalendarDate date) {
        return new CalendarDate(date);
    }

    @Override
    public ICalendarDate newDate(int day, Season season, int year) {
        return new CalendarDate(day, season, year);
    }

    @Override
    public ISeasonData getDataForSeason(Season season) {
        return data.get(season);
    }

    @Override
    public Season getSeasonAtCoordinates(World world, BlockPos pos) {
        return HFTrackers.getCalendar(world).getSeasonAt(pos);
    }
}