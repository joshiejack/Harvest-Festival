package joshie.harvest.quests.gui;

import joshie.harvest.core.base.gui.GuiBaseContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiQuestBoard extends GuiBaseContainer {
    public GuiQuestBoard(EntityPlayer player, InventoryPlayer playerInv) {
        super(new ContainerQuestBoard(player, playerInv), "sign", 56);
    }
}
