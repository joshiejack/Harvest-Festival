package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSyncGold;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerHelper {
    public static void affectStats(EntityPlayer player, double stamina, double fatigue) {
        if (player.worldObj.isRemote) {
            handler.getClient().getPlayerData().affectStats(stamina, fatigue);
        } else handler.getServer().getPlayerData(player).affectStats(stamina, fatigue);
    }

    public static CalendarDate getBirthday(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return handler.getClient().getPlayerData().getBirthday();
        } else return handler.getServer().getPlayerData(player).getBirthday();
    }

    public static int getGold(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return handler.getClient().getPlayerData().getGold();
        } else return handler.getServer().getPlayerData(player).getGold();
    }

    public static void adjustGold(EntityPlayer player, int gold) {
        if (!player.worldObj.isRemote) {
            handler.getServer().getPlayerData(player).addGold(gold);
            PacketHandler.sendToClient(new PacketSyncGold(getGold(player)), (EntityPlayerMP) player);
        }
    }
}
