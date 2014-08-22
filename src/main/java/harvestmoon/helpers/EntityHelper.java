package harvestmoon.helpers;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.server.MinecraftServer;

public class EntityHelper {
    public static EntityAnimal getAnimalFromUUID(UUID uuid) {
        for (int i = 0; i < MinecraftServer.getServer().getEntityWorld().loadedEntityList.size(); i++) {
            Entity entity = (Entity) MinecraftServer.getServer().getEntityWorld().loadedEntityList.get(i);
            if(entity instanceof EntityAnimal) {
                if(entity.getPersistentID().equals(uuid)) {
                    return (EntityAnimal) entity;
                }
            }
        }
        
        return null;
    }
}
