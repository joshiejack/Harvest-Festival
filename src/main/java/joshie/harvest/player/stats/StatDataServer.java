package joshie.harvest.player.stats;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncBirthday;
import joshie.harvest.core.network.PacketSyncGold;
import joshie.harvest.core.network.PacketSyncStats;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class StatDataServer extends StatData {
    public void addGold(EntityPlayerMP player, long gold) {
        long newAmount = this.gold + gold;
        setGold(player, newAmount);
    }
    
    public void setGold(EntityPlayerMP player, long amount) {
        gold = amount;
        HFTrackers.markDirty();
        syncGold(player);
    }
    
    public void sync(EntityPlayerMP player) {
        syncBirthday(player);
        syncGold(player);
        syncStats(player);
    }
    
    private void syncBirthday(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncBirthday(getBirthday()), player);
    }
    
    private void syncGold(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncGold(getGold()), player);
    }
    
    private void syncStats(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncStats(getStamina(), getFatigue(), getStaminaMax(), getFatigueMin()), player);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        birthday.readFromNBT(nbt);
        stamina = nbt.getDouble("Stamina");
        fatigue = nbt.getDouble("Fatigue");
        gold = nbt.getLong("Gold");
        staminaMax = nbt.getDouble("StaminaMax");
        fatigueMin = nbt.getDouble("FatigueMin");
    }

    public void writeToNBT(NBTTagCompound nbt) {
        birthday.writeToNBT(nbt);
        nbt.setDouble("Stamina", stamina);
        nbt.setDouble("Fatigue", fatigue);
        nbt.setLong("Gold", gold);
        nbt.setDouble("StaminaMax", staminaMax);
        nbt.setDouble("FatigueMin", fatigueMin);
    }
}
