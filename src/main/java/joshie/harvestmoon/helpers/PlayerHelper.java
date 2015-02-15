package joshie.harvestmoon.helpers;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSyncGold;
import joshie.harvestmoon.player.PlayerDataClient;
import joshie.harvestmoon.player.PlayerDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PlayerHelper {
    public static void performTask(EntityPlayer player) {
        performTask(player, 1D);
    }

    /** Should always be called client and server side **/
    public static void performTask(EntityPlayer player, double amount) {
        if (player.capabilities.isCreativeMode) return; //If the player is in creative don't exhaust them
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

        affectStats(player, affectStamina ? -amount : 0D, affectFatigue ? amount : 0D);
    }

    public static double getStamina(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return ClientHelper.getPlayerData().getStamina();
        } else return ServerHelper.getPlayerData(player).getStamina();
    }

    public static double getFatigue(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return ClientHelper.getPlayerData().getFatigue();
        } else return ServerHelper.getPlayerData(player).getFatigue();
    }

    public static void affectStats(EntityPlayer player, double stamina, double fatigue) {
        if (player.worldObj.isRemote) {
            ClientHelper.getPlayerData().affectStats(stamina, fatigue);
        } else ServerHelper.getPlayerData(player).affectStats(stamina, fatigue);
    }

    public static CalendarDate getBirthday(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return ClientHelper.getPlayerData().getBirthday();
        } else return ServerHelper.getPlayerData(player).getBirthday();
    }

    public static long getGold(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return ClientHelper.getPlayerData().getGold();
        } else return ServerHelper.getPlayerData(player).getGold();
    }

    public static void adjustGold(EntityPlayer player, long gold) {
        if (!player.worldObj.isRemote) {
            ServerHelper.getPlayerData(player).addGold(gold);
            PacketHandler.sendToClient(new PacketSyncGold(getGold(player)), (EntityPlayerMP) player);
        }
    }

    public static void setGold(EntityPlayer player, long gold) {
        if (!player.worldObj.isRemote) {
            ServerHelper.getPlayerData(player).setGold(gold);
            PacketHandler.sendToClient(new PacketSyncGold(PlayerHelper.getGold(player)), (EntityPlayerMP) player);
        }
    }

    public static void setBirthday(EntityPlayer player, int day, Season season, int year) {
        if (!player.worldObj.isRemote) {
            ServerHelper.getPlayerData(player).setBirthday();
        } else ClientHelper.getPlayerData().setBirthday(day, season, year);
    }

    public static PlayerDataServer getData(EntityPlayer player) {
        return ServerHelper.getPlayerData(player);
    }

    public static PlayerDataClient getData() {
        return ClientHelper.getPlayerData();
    }
}
