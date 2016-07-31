package joshie.harvest.player.stats;

import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.player.packets.PacketSyncBirthday;
import joshie.harvest.player.packets.PacketSyncGold;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class StatDataServer extends StatData {
    public void addGold(EntityPlayerMP player, long gold) {
        long newAmount = this.gold + gold;
        setGold(player, newAmount);
    }
    
    public void setGold(EntityPlayerMP player, long amount) {
        gold = amount;
        syncGold(player);
    }
    
    public void sync(EntityPlayerMP player) {
        syncBirthday(player);
        syncGold(player);
    }
    
    private void syncBirthday(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncBirthday(getBirthday()), player);
    }
    
    private void syncGold(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncGold(getGold()), player);
    }

    public void newDay(long bedtime, long gold) {
        this.gold += gold;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        birthday.readFromNBT(nbt.getCompoundTag("Birthday"));
        gold = nbt.getLong("Gold");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Birthday", birthday.writeToNBT(new NBTTagCompound()));
        nbt.setLong("Gold", gold);
        return nbt;
    }
}
