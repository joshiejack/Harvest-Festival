package joshie.harvest.api.npc;

import joshie.harvest.api.npc.greeting.IGreeting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public interface IInfoButton<E extends EntityAgeable> extends IGreeting<E> {
    /** Get the display name for the button **/
    default String getDisplayName(@Nonnull ItemStack stack) { return stack.getDisplayName(); }

    /** If this button can be displayed currently **/
    default boolean canDisplay(NPC npc, EntityPlayer player) { return true; }

    /** Called serverside when this button is clicked
     *  @return true if we should open a new chat window **/
    default boolean onClicked(NPC npc, EntityPlayer player) { return true; }

    /** Called to draw the info button **/
    @SideOnly(Side.CLIENT)
    default void drawIcon(GuiScreen gui, int x, int y) {}

    @SideOnly(Side.CLIENT)
    default String getTooltip() { return ""; }
}