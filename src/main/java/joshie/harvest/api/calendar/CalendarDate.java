package joshie.harvest.api.calendar;

import net.minecraft.nbt.NBTTagCompound;

public class CalendarDate {
    /** This gets set by the config files, so it won't ALWAYS be 30 */
    public static int DAYS_PER_SEASON = 30;
    private int day;
    private Season season;
    private int year;

    public CalendarDate() {}
    public CalendarDate(int day, Season season, int year) {
        this.day = day;
        this.season = season;
        this.year = year;
    }

    /** The day gets scaled to fit in to the 30 scale mark
     *  For example use something like npc.getBirthday().isSameDay(date);
     *  1 is added to the passed in date, as birthdays and festivals are stored as 1, summer for the 1st of summer
     *  while the actual date system uses 0 to mean day 1
     *  @param date     the date to compare **/
    public boolean isSameDay(CalendarDate date) {
        int day = 1 + (int)Math.ceil(((double)date.getDay() / DAYS_PER_SEASON) * 30);
        return day == this.getDay() && date.getSeason() == this.getSeason();
    }

    /** Make a copy of this date **/
    public CalendarDate copy() {
        return new CalendarDate().setDate(getDay(), getSeason(), getYear());
    }

    /** Update the internal values of this date
     * @param day       the day of the season
     * @param season    the season
     * @param year      the year
     * @return the full date  */
    public CalendarDate setDate(int day, Season season, int year) {
        this.day = day;
        this.season = season;
        this.year = year;
        return this;
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
        return new CalendarDate(day, season, year);
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
