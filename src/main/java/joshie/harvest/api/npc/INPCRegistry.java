package joshie.harvest.api.npc;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.npc.NPC.Age;
import joshie.harvest.npc.NPC.Gender;

import java.util.Collection;

public interface INPCRegistry {
    /** Register a new npc **/
    INPC register(INPC npc);
    
    /** Register a default npc **/
    INPC register(String unlocalised, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor);
    
    /** Returns the collection of all registered npcs **/
    Collection<INPC> getNPCs();

    /** Fetches the npc based on their name, they are
     * registered under.
     * @param string
     * @return
     */
    INPC get(String string);
}