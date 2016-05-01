package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.helpers.CalendarHelper;
import net.minecraft.nbt.NBTTagCompound;

public class CalendarDate implements ICalendarDate {
    private int day;
    private Season season;
    private int year;

    //Cached Value
    private ISeasonData data;

    public CalendarDate() {
    }

    public CalendarDate(int day, Season season, int year) {
        this.day = day;
        this.season = season;
        this.year = year;
        this.data = HFApi.CALENDAR.getDataForSeason(season);
    }

    public CalendarDate(ICalendarDate date) {
        this.day = date.getDay();
        this.season = date.getSeason();
        this.year = date.getYear();
        this.data = date.getSeasonData();
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
            this.data = HFApi.CALENDAR.getDataForSeason(season);
        }

        return this;
    }

    @Override
    public ICalendarDate setYear(int year) {
        this.year = year;
        return this;
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
    public Weekday getWeekday() {
        return Weekday.values()[CalendarHelper.getTotalDays(this) % 7];
    }

    public void readFromNBT(NBTTagCompound nbt) {
        day = nbt.getByte("DayOfMonth");
        season = Season.values()[nbt.getByte("Season")];
        year = nbt.getShort("Year");
        data = HFApi.CALENDAR.getDataForSeason(season);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("DayOfMonth", (byte) day);
        nbt.setByte("Season", (byte) season.ordinal());
        nbt.setShort("Year", (short) year);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ICalendarDate other = (ICalendarDate) obj;
        if (day != other.getDay()) return false;
        if (season != other.getSeason()) return false;
        if (year != other.getYear()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + day;
        result = prime * result + ((season == null) ? 0 : season.hashCode());
        result = prime * result + year;
        return result;
    }
}
