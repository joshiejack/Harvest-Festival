package joshie.harvest.api.player;

import java.util.UUID;

public interface IRelations {
    /** Call this to add or remove relationship points
     *  @param  key   the relatable whose relationship you want to affect
     *  @param  amount      how much to change the relationship by**/
    default void affectRelationship(UUID key, int amount) {}

    /** Returns the relationship value between this player and the relatable
     *  This is a value from 0-65535 (by default) but can be higher based on settings
     *  @param key    the relatable to check the value of*/
    int getRelationship(UUID key);
}