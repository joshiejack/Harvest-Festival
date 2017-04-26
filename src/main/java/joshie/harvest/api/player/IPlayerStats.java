package joshie.harvest.api.player;

public interface IPlayerStats {
    /** Returns the amount of gold this player has **/
    long getGold();

    /** Sets the amount of gold,
     *  Take note that this will only work serverside
     *  and will get synced to the client
     *  @param gold the amount to change the value to */
    default void setGold(long gold) {}
}