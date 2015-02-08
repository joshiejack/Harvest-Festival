package joshie.harvestmoon.buildings.data;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public abstract class EntityData {
    public static HashMap<String, EntityData> data = new HashMap();
    
    public abstract Entity getEntity(World world, int x, int y, int z);

    public static String getStringFor(Entity entity, String name) {
        return data.get(name).getStringFor(entity);
    }
    
    public abstract String getStringFor(Entity e);
    
    static {
        data.put("EntityItemFrame", new DataFrame());
        data.put("EntityPainting", new DataPainting());
    }
}
