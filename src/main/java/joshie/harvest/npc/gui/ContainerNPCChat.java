package joshie.harvest.npc.gui;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.HFApi;
import joshie.harvest.npc.HFNPCs;
import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerNPCChat extends ContainerNPCBase {
    private int nextGui = -1;
    private boolean open = false;

    public ContainerNPCChat(EntityNPC npc, InventoryPlayer playerInventory, int nextGui) {
        super(npc, playerInventory);
        this.nextGui = nextGui;
        this.open = true;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!player.worldObj.isRemote) {
            HFApi.RELATIONS.talkTo(player, npc.getRelatable());
        }

        //Kill the goddess
        if (npc.getNPC() == HFNPCs.GODDESS) {
            npc.setDead();
        }

        if (nextGui != -1 && open) {
            open = false;
            player.openGui(HarvestFestival.instance, nextGui, player.worldObj, npc.getEntityId(), 0, 0);
        }
    }
}
