package harvestmoon.helpers;

import static harvestmoon.HarvestMoon.handler;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerHelper {
    public static void affectStats(EntityPlayer player, double stamina, double fatigue) {
        if (player.worldObj.isRemote) {
            handler.getClient().getPlayerData().affectStats(stamina, fatigue);
        } else handler.getServer().getPlayerData(player).affectStats(stamina, fatigue);
    }
}
