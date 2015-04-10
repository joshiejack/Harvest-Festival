package joshie.harvestmoon.api.npc;

import java.util.Collection;

import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.npc.NPC.Age;
import joshie.harvestmoon.npc.NPC.Gender;

public interface INPCRegistry {
    /** Register a new npc **/
    public INPC register(INPC npc);
    
    /** Register a default npc **/
    public INPC register(String unlocalised, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth);
    
    /** Returns the collection of all registered npcs **/
    public Collection<INPC> getNPCs();

    /** Fetches the npc based on their name, they are
     * registered under.
     * @param string
     * @return
     */
    public INPC get(String string);
}
