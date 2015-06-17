package joshie.harvest.player.fridge;

import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncFridge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class FridgeDataServer extends FridgeData {
    public void sync(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncFridge(this), (player));
    }
}
