package joshie.harvest.api.npc;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

/** This is for conditional based greetings **/
public interface IConditionalGreeting<E extends EntityAgeable> extends IGreeting<E> {
    /** Returns true if this conditional has been met
     *  @param player  the player
     *  @param ageable the npc entity
     *  @param npc     the npc type
     *  @return whether this can be displayed currently**/
    default boolean canDisplay(EntityPlayer player, E ageable, INPC npc) {
        return true;
    }

    /** Return the chance for this to display **/
    default double getDisplayChance() {
        return 5D;
    }
}