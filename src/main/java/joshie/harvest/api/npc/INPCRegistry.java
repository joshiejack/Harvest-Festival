package joshie.harvest.api.npc;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.npc.gift.IGiftRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/** For registering and manipulating npcs **/
public interface INPCRegistry {
    /** Register a default npc
     * @param resource      the registry name, e.g. harvestfestival:jim
     * @param gender        the gender of the npc, this is only used for specialised greetings
     * @param age           the age group of the npc
     * @param dayOfBirth    the date of birth for this npc,
     *                      take note that by default there are 30 days in a season,
     *                      if you use a higher number, this npc will never have
     *                      a birthday unless users change the config value
     * @param seasonOfBirth the season the npcs birthday is in, keep it to spring,summer,autumn or winter
     * @param insideColor   this is the internal border colour of the npcs chat box
     * @param outsideColor  this is the outer border colour of the npcs chat box
     * @return returns the npc, so you can manipulate it further if you like**/
    INPC register(ResourceLocation resource, Gender gender, Age age, int dayOfBirth, Season seasonOfBirth, int insideColor, int outsideColor);

    /** This will return the instance of the gift registry
     * @return the npc gift registry **/
    IGiftRegistry getGifts();

    /** Will return a stack representation of this npc
     *  @param npc the npc **/
    ItemStack getStackForNPC(INPC npc);

    enum Gender {
        MALE, FEMALE
    }

    enum Age {
        CHILD, ADULT, ELDER
    }
}