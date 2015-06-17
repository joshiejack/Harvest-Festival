package joshie.harvest.core.helpers;

import java.util.Random;
import java.util.UUID;

import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.buildings.BuildingStage;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncGold;
import joshie.harvest.player.FridgeContents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
               // MinecraftServer.getServer().getConfigurationManager().respawnPlayer((EntityPlayerMP) player, player.worldObj.provider.dimensionId, true);
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
        return HFTrackers.getPlayerTracker(player).getStats().getStamina();
    }

    public static double getFatigue(EntityPlayer player) {
        return HFTrackers.getPlayerTracker(player).getStats().getFatigue();
    }

    public static void affectStats(EntityPlayer player, double stamina, double fatigue) {
        HFTrackers.getPlayerTracker(player).getStats().affectStats(stamina, fatigue);
    }

    public static ICalendarDate getBirthday(EntityPlayer player) {
        return HFTrackers.getPlayerTracker(player).getStats().getBirthday();
    }

    public static long getGold(EntityPlayer player) {
        return HFTrackers.getPlayerTracker(player).getStats().getGold();
    }

    public static void adjustGold(EntityPlayer player, long gold) {
        if (!player.worldObj.isRemote) {
            HFTrackers.getPlayerTracker(player).getStats().addGold(gold);
            PacketHandler.sendToClient(new PacketSyncGold(getGold(player)), (EntityPlayerMP) player);
        }
    }

    public static void setGold(EntityPlayer player, long gold) {
        if (!player.worldObj.isRemote) {
            HFTrackers.getPlayerTracker(player).getStats().setGold(gold);
            PacketHandler.sendToClient(new PacketSyncGold(PlayerHelper.getGold(player)), (EntityPlayerMP) player);
        }
    }

    public static FridgeContents getFridge(EntityPlayer player) {
        return HFTrackers.getPlayerTracker(player).getFridge();
    }

    public static boolean isOnlineOrFriendsAre(UUID owner) {
        return HFTrackers.getPlayerTracker(owner).getFriendTracker().isOnlineOrFriendsAre();
    }

    public static void addBuilding(World world, BuildingStage building) {
        HFTrackers.getPlayerTracker(building.owner).getTown().addBuilding(world, building);
    }
}
