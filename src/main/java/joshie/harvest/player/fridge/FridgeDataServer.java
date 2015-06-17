package joshie.harvest.player.fridge;

import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncFridge;
import net.minecraft.entity.player.EntityPlayerMP;

public class FridgeDataServer extends FridgeData {
    public void sync(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncFridge(this), (player));
    }
}
