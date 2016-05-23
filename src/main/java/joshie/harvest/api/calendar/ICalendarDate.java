package joshie.harvest.api.calendar;

import joshie.harvest.api.core.ISeasonData;
import net.minecraft.nbt.NBTTagCompound;


public interface ICalendarDate {
    /** Returns the weekday **/
    public Weekday getWeekday();
    
    /** Returns the day **/
    public int getDay();
    
    /** Returns the season **/
    public Season getSeason();
    
    /** Returns data about the season **/
    public ISeasonData getSeasonData();
    
    /** Returns the year **/
    public int getYear();
    
    /** Sets the day **/
    public ICalendarDate setDay(int day);
    
    /** Sets the day **/
    public ICalendarDate setSeason(Season season);
    
    /** Sets the year **/
    public ICalendarDate setYear(int year);

    /** Reads the date from nbt **/
    public void readFromNBT(NBTTagCompound nbt);

    /** Writes the date to nbt **/
    NBTTagCompound writeToNBT(NBTTagCompound nbt);
}