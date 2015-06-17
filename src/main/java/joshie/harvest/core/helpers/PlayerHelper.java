package joshie.harvest.core.helpers;

import java.util.Random;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.stats.StatData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PlayerHelper {
    private static final Random rand = new Random();

    public static void performTask(EntityPlayer player) {
        performTask(player, player.getCurrentEquippedItem(), 1D);
    }

    /** Should always be called client and server side **/
    public static void performTask(EntityPlayer player, ItemStack stack, double amount) {
        ToolHelper.levelTool(stack);

        if (player.capabilities.isCreativeMode) return; //If the player is in creative don't exhaust them
        StatData stats = HFTrackers.getPlayerTracker(player).getStats();
        double stamina = stats.getStamina();
        double fatigue = stats.getFatigue();
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
            stats.affectStats(stamina, fatigue);
        }

        stats.affectStats(affectStamina ? -amount : 0D, affectFatigue ? amount : 0D);
    }
}
