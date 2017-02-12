package joshie.harvest.api.player;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.api.npc.RelationStatus;

public interface IRelations {
    /** If a relation status is met
     *  @param npc          the npc to check
     *  @param status    the status to check**/
    boolean isStatusMet(NPC npc, RelationStatus status);

    /** Call this to add or remove relationship points
     *  @param  npc         the npc whose relationship you want to affect
     *  @param  amount      how much to change the relationship by**/
    default void affectRelationship(NPC npc, int amount) {}

    /** Returns the relationship value between this player and the npc
     *  @param npc  the npc*/
    int getRelationship(NPC npc);
}