package joshie.harvestmoon.gui;

import joshie.harvestmoon.npc.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

public class GuiNPCConfirmation extends GuiNPC {
    private String description;
    private String confirm;
    private String cancel;
    
    public GuiNPCConfirmation(EntityNPC npc, EntityPlayer player, String description, String confirm, String cancel) {
        super(npc, player);
    }
    
    @Override
    protected String getScript() {
        return description + "[" + confirm +  "]     " + "[" + cancel + "]";
    }
    
    @Override
    public void endChat() {
        player.closeScreen();
    }
}
