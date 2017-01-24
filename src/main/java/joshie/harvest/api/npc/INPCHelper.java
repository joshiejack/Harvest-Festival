package joshie.harvest.api.npc;

import joshie.harvest.api.npc.gift.IGiftRegistry;
import joshie.harvest.api.npc.greeting.Script;
import joshie.harvest.api.npc.schedule.ScheduleBuilder;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
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

    /** Called from the schedule builder to build it
     *  @param builder the builder object **/
    ISchedule buildSchedule(ScheduleBuilder builder);

    /** Forces a script to open
     *  @param player the player to open it for
     *  @param npc    the npc to open it for
     *  @param script   the script data to use **/
    void forceScriptOpen(EntityPlayer player, EntityAgeable npc, Script script);

    enum Gender {
        MALE, FEMALE
    }

    enum Age {
        CHILD, ADULT, ELDER
    }
}