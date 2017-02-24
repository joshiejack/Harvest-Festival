package joshie.harvest.player.stats;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.packet.PacketSyncBirthday;
import joshie.harvest.player.packet.PacketSyncGold;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;


public class StatsServer extends Stats {
    private final PlayerTrackerServer master;

    public StatsServer(PlayerTrackerServer master) {
        this.master = master;
    }

    public boolean setBirthday(World world) {
        if (birthday == null) {
            birthday = HFApi.calendar.getDate(world).copy();
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

    @Override
    public void setGold(long amount) {
        setGold(master.getAndCreatePlayer(), amount);
    }
    
    public void sync(EntityPlayerMP player) {
        syncBirthday(player);
        syncGold(player);
    }
    
    private void syncBirthday(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncBirthday(getBirthday()), player);
    }
    
    public void syncGold(EntityPlayerMP player) {
        PacketHandler.sendToClient(new PacketSyncGold(getGold()), player);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        birthday = CalendarDate.fromNBT(nbt.getCompoundTag("Birthday"));
        gold = nbt.getLong("Gold");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Birthday", birthday.toNBT());
        nbt.setLong("Gold", gold);
        return nbt;
    }
}
