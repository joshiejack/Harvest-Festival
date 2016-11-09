package joshie.harvest.api.npc;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IInfoButton<E extends EntityAgeable> extends IGreeting<E> {
    /** Get the display name for the button **/
    default String getDisplayName(ItemStack stack) { return stack.getDisplayName(); }

    /** If this button can be displayed currently **/
    default boolean canDisplay(INPC npc, EntityPlayer player) { return true; }

    /** Called serverside when this button is clicked
     *  @return true if we should open a new chat window **/
    default boolean onClicked(INPC npc, EntityPlayer player) { return true; }
}
