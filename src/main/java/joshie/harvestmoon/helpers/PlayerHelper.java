package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerHelper {
    public static void affectStats(EntityPlayer player, double stamina, double fatigue) {
        if (player.worldObj.isRemote) {
            handler.getClient().getPlayerData().affectStats(stamina, fatigue);
        } else handler.getServer().getPlayerData(player).affectStats(stamina, fatigue);
    }
}
