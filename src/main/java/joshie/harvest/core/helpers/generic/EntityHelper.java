package joshie.harvest.core.helpers.generic;

import java.util.List;
import java.util.UUID;

import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityHelper {
    //Loops through all the animals in the specified dimension id
    public static EntityAnimal getAnimalFromUUID(int dimension, UUID uuid) {
        World world = MCServerHelper.getWorld(dimension);
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = (Entity) world.loadedEntityList.get(i);
            if (entity instanceof EntityAnimal) {
                if (UUIDHelper.getEntityUUID(entity).equals(uuid)) {
                    return (EntityAnimal) entity;
                }
            }
        }
    
        return null;
    }
    
    public static EntityNPCBuilder getBuilderFromUUID(int dimension, UUID uuid) {
        World world = MCServerHelper.getWorld(dimension);
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = (Entity) world.loadedEntityList.get(i);
            if (entity instanceof EntityNPCBuilder) {
                if (UUIDHelper.getEntityUUID(entity).equals(uuid)) {
                    return (EntityNPCBuilder) entity;
                }
            }
        }
    
        return null;
    }

    /** Gets the player from the uuid **/
    public static EntityPlayerMP getPlayerFromUUID(UUID uuid) {        
        //Loops through every single player
        for (EntityPlayer player : (List<EntityPlayerMP>) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList) {
            if (UUIDHelper.getPlayerUUID(player).equals(uuid)) {
                return (EntityPlayerMP) player;
            }
        }
    
        return null;
    }
    
    public static boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer || player.getGameProfile().getName().equals("CoFH") || player.getGameProfile().getName().startsWith("[Thaumcraft");
    }
}
