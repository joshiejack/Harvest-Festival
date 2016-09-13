package joshie.harvest.api.ticking;


/** This is a capability for ticking tile entities, and entities once daily **/
public interface IDailyTickable {
    /** Called when the day ticks over **/
    void newDay(Phase phase);

    /** Return true if this is a priority tickable and should get run first **/
    default boolean isPriority() {
        return false;
    }

    /** The phases are whether this tick occurs before animals and town updates,
     *  or if it occurs after they updated. The mine phase is used when randomly,
     *  updating the mine, instead of when it changes over normally */
    enum Phase {
        PRE, POST, MINE
    }
}
