package joshie.harvest.api.calendar;

import net.minecraft.nbt.NBTTagCompound;

public class CalendarDate {
    /** This gets set by the config files, so it won't ALWAYS be 30 */
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

    /** Make a copy of this date **/
    public CalendarDate copy() {
        return new CalendarDate().setDate(weekday, day, season, year);
    }

    /** Update the internal values of this date
     * @param weekday   the day of the week
     * @param day       the day of the season
     * @param season    the season
     * @param year      the year
     * @return the full date  */
    public CalendarDate setDate(Weekday weekday, int day, Season season, int year) {
        this.weekday = weekday;
        this.day = day;
        this.season = season;
        this.year = year;
        return this;
    }

    /** @return  the day of the week **/
    public Weekday getWeekday() {
        return weekday;
    }

    /** @return  the day of the season **/
    public int getDay() {
        return day;
    }

    /** @return  the season **/
    public Season getSeason() {
        return season;
    }

    /** @return  the year **/
    public int getYear() {
        return year;
    }

    /** Load a date from a nbt tag
     * @param nbt   the tag to read
     * @return the date */
    public static CalendarDate fromNBT(NBTTagCompound nbt) {
        Weekday weekday = Weekday.values()[nbt.getByte("WeekDay")];
        int day = nbt.getInteger("Day");
        Season season = Season.values()[nbt.getByte("Season")];
        int year = nbt.getInteger("Year");
        return new CalendarDate().setDate(weekday, day, season, year);
    }

    /** Save a date to nbt **/
    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
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
        return day == that.day && year == that.year && season == that.season;
    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + (season != null ? season.hashCode() : 0);
        result = 31 * result + year;
        return result;
    }
}