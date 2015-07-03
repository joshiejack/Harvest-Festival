package joshie.harvest.npc.gui;

import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerNPCSelect extends ContainerNPCBase {
    public ContainerNPCSelect(EntityNPC npc, InventoryPlayer playerInventory) {
        super(npc, playerInventory);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
    }
}
