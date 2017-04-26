package joshie.harvest.api.npc.greeting;

import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;

public interface IGreeting<E extends EntityAgeable> {
    /** Returns the localized name for this text
     * @param player     the player
     * @param ageable    the npcs entity
     * @param npc        the npcs representation */
    @SuppressWarnings("deprecation")
    String getLocalizedText(EntityPlayer player, E ageable, NPC npc);
}