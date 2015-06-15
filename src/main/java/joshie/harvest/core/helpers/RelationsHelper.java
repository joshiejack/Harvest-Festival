package joshie.harvest.core.helpers;

import java.util.UUID;

import joshie.harvest.api.npc.INPC;
import joshie.harvest.npc.EntityNPC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class RelationsHelper {
    public static final int ADJUSTED_MAX = 65535;

    /** Returns an adjusted relationship from 0-65,535 */
    public static int getRelationshipValue(EntityLivingBase entity, EntityPlayer player) {
        if (entity.worldObj.isRemote) {
            return 1 + Short.MAX_VALUE + ClientHelper.getPlayerData().getRelationship(entity);
        } else return 1 + Short.MAX_VALUE + ServerHelper.getPlayerData(player).getRelationship(entity);
    }
    
    public static int getRelationshipValue(EntityLivingBase entity, UUID player) {
        if (entity.worldObj.isRemote) {
            return 1 + Short.MAX_VALUE + ClientHelper.getPlayerData().getRelationship(entity);
        } else return 1 + Short.MAX_VALUE + ServerHelper.getPlayerData(player).getRelationship(entity);
    }

    public static void removeRelations(EntityLivingBase entity) {
        if (entity.worldObj.isRemote) {
            ClientHelper.getPlayerData().removeRelations(entity);
        } else {
            RelationsHelper.removeAllRelations(entity);
        }
    }

    public static void removeAllRelations(EntityLivingBase entity) {
        ServerHelper.removeAllRelations(entity);
    }

    /** Should in practice only be called server side **/
    public static void affectRelations(EntityPlayer player, EntityLivingBase entity, int amount) {
        if (!entity.worldObj.isRemote) {
            ServerHelper.getPlayerData(player).affectRelationship(entity, 1000);
        }
    }

    public static void setTalkedTo(EntityPlayer player, EntityLivingBase entity) {
        if (!player.worldObj.isRemote) {
            ServerHelper.getPlayerData(player).setTalkedTo(entity);
        }
    }

    public static void setGifted(EntityPlayer player, INPC npc, int points) {
        ServerHelper.getPlayerData(player).setGifted(npc, points);
    }

    public static void setMarried(EntityNPC npc, EntityPlayer player) {
        ServerHelper.getPlayerData(player).setMarried(npc);
    }
}
