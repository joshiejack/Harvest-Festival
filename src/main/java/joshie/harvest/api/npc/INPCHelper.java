package joshie.harvest.api.npc;

import joshie.harvest.api.npc.gift.IGiftRegistry;
import net.minecraft.item.ItemStack;

/** For registering and manipulating npcs **/
public interface INPCHelper {
    /** This will return the instance of the gift registry
     * @return the npc gift registry **/
    IGiftRegistry getGifts();

    /** Will return a stack representation of this npc
     *  @param npc the npc **/
    ItemStack getStackForNPC(NPC npc);

    /** Speech helper
     *  Returns a random speech for this npc based on the input string
     *  @param npc the npc
     *  @param text the unlocalzied name
     *  @param maximumAlternatives max attempts to try
     *  @param data any additional data to be formatted **/
    String getRandomSpeech(NPC npc, final String text, final int maximumAlternatives, Object... data);

    enum Gender {
        MALE, FEMALE
    }

    enum Age {
        CHILD, ADULT, ELDER
    }
}