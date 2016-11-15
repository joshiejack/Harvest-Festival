package joshie.harvest.api.player;

import joshie.harvest.api.knowledge.Note;

public interface IPlayerTracking {
    /** Call on the client and server, as this is NOT synced
     *
     * @param note the note for the player to learn
     * @return returns true if the note was newly learnt, false if the player already knows it*/
    boolean learnNote(Note note);
}
