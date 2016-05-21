package joshie.harvest.api.core;


import net.minecraft.world.World;

/** This is a capability for ticking tile entities, and entities once daily **/
public interface IDailyTickable {
    /** Called when the day ticks over
     *  @param world, always use this, rather than other types
     *  @return return true if the tile should still tick daily**/
    void newDay(World world);

    /** Return true if this is invalid **/
    boolean isInvalid();
}
