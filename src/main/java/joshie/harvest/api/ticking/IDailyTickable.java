package joshie.harvest.api.ticking;


/** This is a capability for ticking tile entities, and entities once daily **/
public interface IDailyTickable {
    /** The phase the tickable should run in **/
    Phase[] getPhases();

    /** Called when the new day ticks over **/
    void newDay();

    /** PRIORITY = occurs before tickable blocks tick
     *  PRE = occurs after the tick blocks tick and before animals, and town ticks
     *  POST = occurs after the animal and town ticks
     *  LAST = occurs after the post stage*/
    enum Phase {
        PRIORITY, PRE, POST, LAST
    }
}
