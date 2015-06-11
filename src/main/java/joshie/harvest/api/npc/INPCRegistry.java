package joshie.harvest.api.npc;

import java.util.Collection;

import joshie.harvest.api.core.Season;
import joshie.harvest.npc.NPC.Age;
import joshie.harvest.npc.NPC.Gender;

public interface INPCRegistry {
    /** Register a new npc **/
    public INPC register(INPC npc);
    
    /** Register a default npc **/
    public INPC register(String unlocalised, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor);
    
    /** Returns the collection of all registered npcs **/
    public Collection<INPC> getNPCs();

    /** Fetches the npc based on their name, they are
     * registered under.
     * @param string
     * @return
     */
    public INPC get(String string);
}
