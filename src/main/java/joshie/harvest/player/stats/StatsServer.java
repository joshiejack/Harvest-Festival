package joshie.harvest.player.stats;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.player.packets.PacketSyncBirthday;
import joshie.harvest.player.packets.PacketSyncGold;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static joshie.harvest.api.calendar.Season.NETHER;

public class StatsServer extends Stats {
    private boolean isBirthdaySet() {
        return birthday.getSeason() != NETHER && birthday.getDay() != 0 && birthday.getYear() != 0;
    }

    public boolean setBirthday(World world) {
        if (!isBirthdaySet()) {
            birthday = HFApi.calendar.cloneDate(HFApi.calendar.getDate(world));
            return true;
        } else return false;
    }

    public void addGold(@Nullable EntityPlayerMP player, long gold) {
        long newAmount = this.gold + gold;
        setGold(player, newAmount);
    }
    
    public void setGold(@Nullable EntityPlayerMP player, long amount) {
        gold = amount;
        if (player != null) syncGold(player);
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
