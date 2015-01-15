package joshie.harvestmoon.helpers;

import static joshie.harvestmoon.HarvestMoon.handler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class RelationsHelper {
    public static final int ADJUSTED_MAX = 65535;
    /** Returns an adjusted relationship from 0-65,535 */
    public static int getRelationshipValue(EntityLivingBase entity, EntityPlayer player) {
        if (entity.worldObj.isRemote) {
            return 1 + Short.MAX_VALUE + handler.getClient().getPlayerData().getRelationship(entity);
        } else return 1 + Short.MAX_VALUE + handler.getServer().getPlayerData(player).getRelationship(entity);
    }

    public static void removeRelations(EntityLivingBase entity) {
        if(entity.worldObj.isRemote) {
            handler.getClient().getPlayerData().removeRelations(entity);
        } else {
            handler.getServer().removeAllRelations(entity);
        }
    }
}
