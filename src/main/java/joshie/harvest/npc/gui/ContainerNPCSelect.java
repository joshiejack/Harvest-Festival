package joshie.harvest.npc.gui;

import joshie.harvest.npc.entity.AbstractEntityNPC;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerNPCSelect extends ContainerNPCBase {
    public ContainerNPCSelect(AbstractEntityNPC npc, InventoryPlayer playerInventory) {
        super(npc, playerInventory);
    }
}
