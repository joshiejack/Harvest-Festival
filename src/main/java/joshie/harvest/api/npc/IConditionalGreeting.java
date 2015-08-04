package joshie.harvest.api.npc;

import net.minecraft.entity.player.EntityPlayer;

public interface IConditionalGreeting {
    /** Returns true if this conditional has been met **/
    public boolean canDisplay(EntityPlayer player);

    /** Returns the text for this conditional **/
    public String getText();
    
    /** Return the priority **/
    public int getPriority();
}
