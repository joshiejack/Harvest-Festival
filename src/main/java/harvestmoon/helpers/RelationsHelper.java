package harvestmoon.helpers;

import static harvestmoon.HarvestMoon.handler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class RelationsHelper {
    public static int getRelationship(EntityLivingBase entity, EntityPlayer player) {
        if (entity.worldObj.isRemote) {
            return handler.getClient().getPlayerData().getRelationship(entity);
        } else return handler.getServer().getPlayerData(player).getRelationship(entity);
    }

    public static void removeRelations(EntityLivingBase entity) {
        if(entity.worldObj.isRemote) {
            handler.getClient().getPlayerData().removeRelations(entity);
        } else {
            handler.getServer().removeAllRelations(entity);
        }
    }
}
