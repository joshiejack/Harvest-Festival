package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSyncGold;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerHelper {
    public static void performTask(EntityPlayer player) {
        performTask(player, 1D);
    }

    /** Should always be called client and server side **/
    public static void performTask(EntityPlayer player, double amount) {
        double stamina = getStamina(player);
        double fatigue = getFatigue(player);
        boolean affectFatigue = false;
        boolean affectStamina = false;
                
        if (stamina >= 1) {
            affectStamina = true;
        } else if (fatigue < 255) {
            affectFatigue = true;
        } else {
            //If the players fatigue > 255, Make them faint
            if (!player.worldObj.isRemote) {
                MinecraftServer.getServer().getConfigurationManager().respawnPlayer((EntityPlayerMP) player, player.worldObj.provider.dimensionId, true);
            }
            
            fatigue = -fatigue + 100;
            stamina = -stamina + 20;
            affectStats(player, stamina, fatigue);
        }
                
        affectStats(player, affectStamina? -amount: 0D, affectFatigue? amount: 0D);
    }

    public static double getStamina(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return handler.getClient().getPlayerData().getStamina();
        } else return handler.getServer().getPlayerData(player).getStamina();
    }

    public static double getFatigue(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return handler.getClient().getPlayerData().getFatigue();
        } else return handler.getServer().getPlayerData(player).getFatigue();
    }

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
