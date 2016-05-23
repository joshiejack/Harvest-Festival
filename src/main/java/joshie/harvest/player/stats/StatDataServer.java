package joshie.harvest.player.stats;

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

    public void newDay(long bedtime, long gold) {
        long hours = 23999 - (bedtime - 6000);
        double fatigueChange = 0D;
        double staminaChange = 0D;
        if (hours < 100) fatigueChange = -20D;
        else {
            fatigueChange = (2 + ((hours/1000) * 2));
            staminaChange = ((hours / 100) + 34);
        }
        
        affectStats(staminaChange, fatigueChange);
        this.gold += gold;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        birthday.readFromNBT(nbt.getCompoundTag("Birthday"));
        stamina = nbt.getDouble("Stamina");
        fatigue = nbt.getDouble("Fatigue");
        gold = nbt.getLong("Gold");
        staminaMax = nbt.getDouble("StaminaMax");
        fatigueMin = nbt.getDouble("FatigueMin");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Birthday", birthday.writeToNBT(new NBTTagCompound()));
        nbt.setDouble("Stamina", stamina);
        nbt.setDouble("Fatigue", fatigue);
        nbt.setLong("Gold", gold);
        nbt.setDouble("StaminaMax", staminaMax);
        nbt.setDouble("FatigueMin", fatigueMin);
        return nbt;
    }
}
