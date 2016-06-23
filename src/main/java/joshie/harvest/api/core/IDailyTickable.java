package joshie.harvest.api.core;


/** This is a capability for ticking tile entities, and entities once daily **/
public interface IDailyTickable {
    /** Called when the day ticks over
     *  @return return true if the tile should still tick daily**/
    void newDay();

    /** Return true if this is invalid **/
    boolean isInvalid();

    /** Return true if this is a priority tickable and should get run first **/
    default boolean isPriority() {
        return false;
    }
}
