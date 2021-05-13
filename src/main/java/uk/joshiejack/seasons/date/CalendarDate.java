package uk.joshiejack.seasons.date;

import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.time.DayOfWeek;

public class CalendarDate implements INBTSerializable<NBTTagCompound> {
    private DayOfWeek weekday = DayOfWeek.MONDAY;
    private int monthday = 1;
    private int year = 1;

    public CalendarDate() {}
    public CalendarDate(DayOfWeek weekday, int monthday, int year) {
        this.weekday = weekday;
        this.monthday = monthday;
        this.year = year;
    }

    @Nonnull
    public DayOfWeek getWeekday() {
        if (weekday == null) {
            weekday = DayOfWeek.MONDAY;
        }

        return weekday;
    }

    public int getDay() {
        return monthday;
    }

    public int getYear() {
        return year;
    }

    public void set(CalendarDate date) {
        this.weekday = date.weekday;
        this.monthday = date.monthday;
        this.year = date.year;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("Weekday", (byte) getWeekday().ordinal());
        tag.setInteger("Day", monthday);
        tag.setInteger("Year", year);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        weekday = DayOfWeek.values()[tag.getByte("Weekday")];
        monthday = tag.getInteger("Day");
        year = tag.getInteger("Year");
    }

    public static CalendarDate getFromTime(long time) {
        return new CalendarDate(TimeHelper.getWeekday(time), 1 + DateHelper.getDay(time), 1 + DateHelper.getYear(time));
    }
}
