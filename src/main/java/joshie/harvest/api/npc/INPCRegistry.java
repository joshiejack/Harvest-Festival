package joshie.harvest.api.npc;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.INPC.Age;
import joshie.harvest.api.npc.INPC.Gender;
import net.minecraft.util.ResourceLocation;

public interface INPCRegistry {
    /** Register a default npc **/
    INPC register(ResourceLocation resource, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor);
}