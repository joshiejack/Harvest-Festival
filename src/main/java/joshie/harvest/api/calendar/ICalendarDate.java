package joshie.harvest.api.calendar;

import joshie.harvest.api.core.ISeasonData;
import net.minecraft.nbt.NBTTagCompound;


public interface ICalendarDate {
    /** Returns the weekday **/
    Weekday getWeekday();
    
    /** Returns the day **/
    int getDay();
    
    /** Returns the season **/
    Season getSeason();
    
    /** Returns data about the season **/
    ISeasonData getSeasonData();
    
    /** Returns the year **/
    int getYear();

    /** Sets the weekday **/
    ICalendarDate setWeekday(Weekday weekday);
    
    /** Sets the day **/
    ICalendarDate setDay(int day);
    
    /** Sets the day **/
    ICalendarDate setSeason(Season season);
    
    /** Sets the year **/
    ICalendarDate setYear(int year);

    /** Reads the date from nbt **/
    void readFromNBT(NBTTagCompound nbt);

    /** Writes the date to nbt **/
    NBTTagCompound writeToNBT(NBTTagCompound nbt);
}