package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.helpers.CalendarHelper;
import net.minecraft.nbt.NBTTagCompound;

public class CalendarDate implements ICalendarDate {
    private Weekday weekday;
    private int day;
    private Season season;
    private int year;

    //Cached Value
    private ISeasonData data;

    public CalendarDate() {}
    public CalendarDate(int day, Season season, int year) {
        this.day = day;
        this.season = season;
        this.year = year;
        this.weekday = CalendarHelper.getWeekday(CalendarHelper.getTotalDays(day, season, year));
        this.data = HFApi.calendar.getDataForSeason(season);
    }

    public CalendarDate(ICalendarDate date) {
        this.weekday = date.getWeekday();
        this.day = date.getDay();
        this.season = date.getSeason();
        this.year = date.getYear();
        this.data = date.getSeasonData();
    }

    @Override
    public ICalendarDate setWeekday(Weekday weekday) {
        this.weekday = weekday;
        return this;
    }

    @Override
    public ICalendarDate setDay(int day) {
        this.day = day;
        return this;
    }

    @Override
    public ICalendarDate setSeason(Season season) {
        Season previous = this.season;
        this.season = season;
        if (previous != season) {
            this.data = HFApi.calendar.getDataForSeason(season);
        }

        return this;
    }

    @Override
    public ICalendarDate setYear(int year) {
        this.year = year;
        return this;
    }

    @Override
    public Weekday getWeekday() {
        return weekday;
    }

    @Override
    public int getDay() {
        return day;
    }

    @Override
    public Season getSeason() {
        return season;
    }

    @Override
    public ISeasonData getSeasonData() {
        return data;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        weekday = Weekday.values()[nbt.getByte("WeekDay")];
        day = nbt.getInteger("Day");
        season = Season.values()[nbt.getByte("Season")];
        year = nbt.getInteger("Year");
        data = HFApi.calendar.getDataForSeason(season);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("WeekDay", (byte) weekday.ordinal());
        nbt.setInteger("Day", day);
        nbt.setByte("Season", (byte) season.ordinal());
        nbt.setInteger("Year", year);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarDate that = (CalendarDate) o;
        if (day != that.day) return false;
        if (year != that.year) return false;
        if (weekday != that.weekday) return false;
        return season == that.season;

    }

    @Override
    public int hashCode() {
        int result = weekday != null ? weekday.hashCode() : 0;
        result = 31 * result + day;
        result = 31 * result + (season != null ? season.hashCode() : 0);
        result = 31 * result + year;
        return result;
    }
}
