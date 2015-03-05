package joshie.harvestmoon.core.helpers;

import java.util.Random;
import java.util.UUID;

import joshie.harvestmoon.buildings.BuildingStage;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketSyncGold;
import joshie.harvestmoon.player.FridgeContents;
import joshie.harvestmoon.player.PlayerDataClient;
import joshie.harvestmoon.player.PlayerDataServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class PlayerHelper {
    private static final Random rand = new Random();

    public static void performTask(EntityPlayer player) {
        performTask(player, player.getCurrentEquippedItem(), 1D);
    }

    /** Should always be called client and server side **/
    public static void performTask(EntityPlayer player, ItemStack stack, double amount) {
        ToolHelper.levelTool(stack);

        if (player.capabilities.isCreativeMode) return; //If the player is in creative don't exhaust them
        double stamina = getStamina(player);
        double fatigue = getFatigue(player);
        boolean affectFatigue = false;
        boolean affectStamina = false;

        if (stamina >= 1) {
            affectStamina = true;
        } else if (fatigue < 255) {
            player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 4, true));
            player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 200, 0, true));
            if (rand.nextInt((int) (Math.max(1, 255 - fatigue))) == 0) {
                player.addPotionEffect(new PotionEffect(Potion.confusion.id, 250, 1, true));
            }

            if (fatigue > 250) {
                player.addPotionEffect(new PotionEffect(Potion.blindness.id, 500, 1, true));
                player.addPotionEffect(new PotionEffect(Potion.confusion.id, 750, 1, true));
            }

            affectFatigue = true;
        } else {
            //If the players fatigue > 255, Make them faint
            if (!player.worldObj.isRemote) {
                MinecraftServer.getServer().getConfigurationManager().respawnPlayer((EntityPlayerMP) player, player.worldObj.provider.dimensionId, true);
            }

            player.addPotionEffect(new PotionEffect(Potion.blindness.id, 500, 1, true));
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 750, 1, true));

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

    /** CAN AND WILL RETURN NULL, IF THE UUID COULD NOT BE FOUND **/
    public static PlayerDataServer getData(UUID uuid) {
        return ServerHelper.getPlayerData(uuid);
    }

    public static PlayerDataClient getData() {
        return ClientHelper.getPlayerData();
    }

    public static FridgeContents getFridge(EntityPlayer player) {
        return !player.worldObj.isRemote ? getData(player).getFridge() : ClientHelper.getPlayerData().getFridge();
    }

    public static boolean isOnlineOrFriendsAre(UUID owner) {
        PlayerDataServer data = getData(owner);
        if (data == null) return false;
        else {
            return data.isOnlineOrFriendsAre();
        }
    }

    public static boolean isElligibleToMarry(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return getData().canMarry();
        } else return getData(player).canMarry();
    }

    public static void addBuilding(World world, BuildingStage building) {
        PlayerDataServer data = getData(building.owner);
        if (data != null) {
            data.addBuilding(world, building);
        }
    }
}
