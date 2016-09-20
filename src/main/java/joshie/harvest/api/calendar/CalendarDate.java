package joshie.harvest.api.calendar;

import net.minecraft.nbt.NBTTagCompound;

public class CalendarDate {
    /** This gets set by the config files,
     *  so it won't ALWAYS be 30 */
    public static int DAYS_PER_SEASON = 30;
    private Weekday weekday;
    private int day;
    private Season season;
    private int year;

    public CalendarDate() {}
    public CalendarDate(int day, Season season, int year) {
        this.day = day;
        this.season = season;
        this.year = year;
        this.weekday = Weekday.MONDAY;
    }

    private CalendarDate(CalendarDate date) {
        this.weekday = date.getWeekday();
        this.day = date.getDay();
        this.season = date.getSeason();
        this.year = date.getYear();
    }

    /** The day gets scaled to fit in to the 30 scale mark **/
    public boolean isSameDay(CalendarDate date) {
        int day = (int)Math.ceil((date.getDay() / DAYS_PER_SEASON) * 30);
        return day == this.getDay() && date.getSeason() == this.getSeason();
    }

    public CalendarDate copy() {
        return new CalendarDate(this);
    }

    public CalendarDate setWeekday(Weekday weekday) {
        this.weekday = weekday;
        return this;
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

    public Weekday getWeekday() {
        return weekday;
    }

    public int getDay() {
        return day;
    }

    public Season getSeason() {
        return season;
    }

    public int getYear() {
        return year;
    }

    public static CalendarDate fromNBT(NBTTagCompound nbt) {
        Weekday weekday = Weekday.values()[nbt.getByte("WeekDay")];
        int day = nbt.getInteger("Day");
        Season season = Season.values()[nbt.getByte("Season")];
        int year = nbt.getInteger("Year");
        return new CalendarDate(day, season, year).setWeekday(weekday);
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
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
        return season == that.season;
    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + (season != null ? season.hashCode() : 0);
        result = 31 * result + year;
        return result;
    }
}
