package joshie.harvest.api.npc;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

/** This is for conditional based greetings **/
public interface IConditionalGreeting<E extends EntityAgeable> {
    /** @return  the max number of alternatives this can look for in the lang **/
    default int getMaximumAlternatives() {
        return 1;
    }

    /** Returns true if this conditional has been met
     *  @param player  the player
     *  @param ageable the npc entity
     *  @param npc     the npc type
     *  @return whether this can be displayed currently**/
    default boolean canDisplay(EntityPlayer player, E ageable, INPC npc) {
        return true;
    }

    /** Returns the text for this conditional,
     *  @param player  the player
     *  @param ageable the npc entity
     *  @param npc     the npc type
     * @return the UNLOCALIZED text **/
    String getUnlocalizedText(EntityPlayer player, E ageable, INPC npc);

    /** Returns the localized name for this text
     * @param text     the text**/
    @SuppressWarnings("deprecation")
    default String getLocalizedText(EntityPlayer player, E ageable, INPC npc, String text) {
        return I18n.translateToLocal(text);
    }

    /** Return the chance for this to display **/
    default double getDisplayChance() {
        return 5D;
    }
}