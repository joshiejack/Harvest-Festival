package joshie.harvest.npc.gui;

import joshie.harvest.core.util.ContainerBase;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerNPCBase extends ContainerBase {
    protected EntityNPC npc;

    public ContainerNPCBase(EntityNPC npc, InventoryPlayer playerInventory) {
        this.npc = npc;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        npc.setTalking((EntityPlayer) null);
    }
}
