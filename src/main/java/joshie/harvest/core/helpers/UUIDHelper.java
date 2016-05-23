package joshie.harvest.core.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.UsernameCache;

import java.util.Map;
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

    public static UUID getLastKnownUUID(EntityPlayer player) {
        String name = player.getGameProfile().getName();
        for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }

        return UUIDHelper.getPlayerUUID(player);
    }
}