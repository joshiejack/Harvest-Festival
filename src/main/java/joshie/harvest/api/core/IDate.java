package joshie.harvest.api.core;

import joshie.harvest.calendar.Season;

public interface IDate {
    /** Returns the day **/
    public int getDay();
    
    /** Returns the season **/
    public Season getSeason();
}
