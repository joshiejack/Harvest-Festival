package joshie.harvestmoon.api.core;

import joshie.harvestmoon.calendar.Season;

public interface IDate {
    /** Returns the day **/
    public int getDay();
    
    /** Returns the season **/
    public Season getSeason();
}
