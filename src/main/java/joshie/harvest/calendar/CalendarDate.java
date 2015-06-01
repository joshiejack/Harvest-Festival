package joshie.harvest.calendar;

import joshie.harvest.api.core.IDate;
import joshie.harvest.api.core.Weekday;
import joshie.harvest.core.helpers.CalendarHelper;
import net.minecraft.nbt.NBTTagCompound;

public class CalendarDate implements IDate {
    private int day;
    private Season season;
    private int year;

    public CalendarDate() {}

    public CalendarDate(int day, Season season, int year) {
        this.day = day;
        this.season = season;
        this.year = year;
    }

    public CalendarDate(CalendarDate date) {
        this.day = date.day;
        this.season = date.season;
        this.year = date.year;
    }

    public CalendarDate setDay(int day) {
        this.day = day;
        return this;
    }

    public CalendarDate setSeason(Season season) {
        this.season = season;
        return this;
    }

    public CalendarDate setYear(int year) {
        this.year = year;
        return this;
    }

    public boolean isSet() {
        return season != null && day != 0 && year != 0;
    }

    public int getDay() {
        return day;
    }

    @Override
    public Season getSeason() {
        return season;
    }

    public int getYear() {
        return year;
    }
    
    public Weekday getWeekday() {
        return Weekday.values()[CalendarHelper.getTotalDays(this) % 7];
    }

    public void readFromNBT(NBTTagCompound nbt) {
        day = nbt.getByte("DayOfMonth");
        season = Season.values()[nbt.getByte("Season")];
        year = nbt.getShort("Year");
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
        CalendarDate other = (CalendarDate) obj;
        if (day != other.day) return false;
        if (season != other.season) return false;
        if (year != other.year) return false;
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
