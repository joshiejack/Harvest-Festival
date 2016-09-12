package joshie.harvest.api.ticking;


/** This is a capability for ticking tile entities, and entities once daily **/
public interface IDailyTickable {
    /** Called when the day ticks over **/
    void newDay(Phase phase);

    /** Return true if this is a priority tickable and should get run first **/
    default boolean isPriority() {
        return false;
    }

    enum Phase {
        PRE_ANIMALS, POST_ANIMALS
    }
}
