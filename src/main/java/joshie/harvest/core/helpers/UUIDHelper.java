package joshie.harvest.core.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class UUIDHelper {
    public static UUID getPlayerUUID(EntityPlayer player) {
        return EntityPlayer.getUUID(player.getGameProfile());
        //return player.getUniqueID();
        //return player.getPersistentID();
    }

    public static UUID getEntityUUID(Entity entity) {
        return entity.getPersistentID();
    }
}