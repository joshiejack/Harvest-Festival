package joshie.harvest.api.npc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;

/** This is for conditional based greetings **/
public interface IConditionalGreeting {
    /** @return  the max number of alternatives this can look for in the lang **/
    default int getMaximumAlternatives() {
        return 1;
    }

    /** Returns true if this conditional has been met
     *  @param player  the player
     *  @return whether this can be displayed currently**/
    default boolean canDisplay(EntityPlayer player) {
        return true;
    }

    /** Returns the text for this conditional,
     * @return the UNLOCALIZED text **/
    String getUnlocalizedText();

    /** Returns the localized name for this text
     * @param text     the text**/
    default String getLocalizedText(String text) {
        return I18n.translateToLocal(text);
    }

    /** Return the chance for this to display **/
    default double getDisplayChance() {
        return 5D;
    }
}