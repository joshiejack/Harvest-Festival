package joshie.harvest.api.npc;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.npc.NPC.Age;
import joshie.harvest.npc.NPC.Gender;
import net.minecraft.util.ResourceLocation;

public interface INPCRegistry {
    /** Register a default npc **/
    INPC register(ResourceLocation resource, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor);
}