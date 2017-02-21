package joshie.harvest.shops.gui;

import joshie.harvest.core.base.gui.ContainerBase;
import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerNPCShop extends ContainerBase {
    private final EntityNPC npc;
    private boolean hasBeenClosed = false;

    public ContainerNPCShop(EntityPlayer player, EntityNPC npc) {
        this.npc = npc;
        this.npc.setTalking(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        npc.setTalking(null);
        if (!hasBeenClosed) {
            hasBeenClosed = true;

        }
    }
}
