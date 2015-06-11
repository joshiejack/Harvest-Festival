package joshie.harvest.api.core;


public interface IDate {
    /** Returns the day **/
    public int getDay();
    
    /** Returns the season **/
    public Season getSeason();
}
