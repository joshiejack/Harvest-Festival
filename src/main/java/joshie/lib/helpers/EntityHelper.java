package joshie.lib.helpers;

import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class EntityHelper {
    //Whether the entity is currently in water or not
    public static boolean isInWater(EntityLivingBase entity) {
        double d0 = entity.posY - 0.35F;
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_float(MathHelper.floor_double(d0));
        int k = MathHelper.floor_double(entity.posZ);
        Block block = entity.worldObj.getBlock(i, j, k);
        if (block != null && block.getMaterial() == Material.water) {
            double filled = 1;
            if (filled < 0) {
                filled *= -1;
                return d0 > j + (1 - filled);
            } else return d0 < j + filled;
        } else return false;
    }

    //Whether the entity is in air
    public static boolean isInAir(EntityLivingBase entity) {
        double d0 = entity.posY - 0.35F;
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_float(MathHelper.floor_double(d0));
        int k = MathHelper.floor_double(entity.posZ);
        Block block = entity.worldObj.getBlock(i, j, k);
        if (block != null && block.getMaterial() == Material.air) {
            double filled = 1;
            if (filled < 0) {
                filled *= -1;
                return d0 > j + (1 - filled);
            } else return d0 < j + filled;
        } else return false;
    }

    //Loops through all the animals in the specified dimension id
    public static EntityAnimal getAnimalFromUUID(int dimension, UUID uuid) {
        World world = ServerHelper.getWorld(dimension);
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = (Entity) world.loadedEntityList.get(i);
            if (entity instanceof EntityAnimal) {
                if (entity.getPersistentID().equals(uuid)) {
                    return (EntityAnimal) entity;
                }
            }
        }
    
        return null;
    }

    //Returns the entity id from it's uuid
    public static int getEntityIDFromUUID(UUID uuid) {
        for (WorldServer world : DimensionManager.getWorlds()) {
            for (int i = 0; i < world.loadedEntityList.size(); i++) {
                Entity entity = (Entity) world.loadedEntityList.get(i);
                if (entity instanceof EntityLivingBase) {
                    if (entity.getPersistentID().equals(uuid)) {
                        return entity.getEntityId();
                    }
                }
            }
        }
    
        return 0;
    }

    /** Gets the player from the uuid **/
    public static EntityPlayerMP getPlayerFromUUID(UUID uuid) {
        //Loops through every single player
        for (EntityPlayer player : (List<EntityPlayer>) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList) {
            if (player.getPersistentID().equals(uuid)) {
                return (EntityPlayerMP) player;
            }
        }
    
        return null;
    }

}
