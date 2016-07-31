package joshie.harvest.core.helpers.generic;

import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.util.HFTeleporter;
import joshie.harvest.npc.entity.EntityNPCBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;
import java.util.UUID;

public class EntityHelper {
    //Loops through all the animals in the specified dimension id
    public static EntityAnimal getAnimalFromUUID(int dimension, UUID uuid) {
        World world = MCServerHelper.getWorld(dimension);
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = world.loadedEntityList.get(i);
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
            Entity entity = world.loadedEntityList.get(i);
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
        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList()) {
            if (UUIDHelper.getPlayerUUID(player).equals(uuid)) {
                return (EntityPlayerMP) player;
            }
        }

        return null;
    }

    public static boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer || player.getGameProfile().getName().equals("CoFH") || player.getGameProfile().getName().startsWith("[Thaumcraft");
    }

    public static <T extends Entity> List<T> getEntities(Class<? extends T> t, World world, double size) {
        return world.getEntitiesWithinAABB(t, Block.FULL_BLOCK_AABB.expand(size, size, size));
    }

    public static boolean teleport(Entity entity, int dimension, BlockPos spawn) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer oldWorld = server.worldServerForDimension(entity.getEntityWorld().provider.getDimension());
        WorldServer newWorld = server.worldServerForDimension(dimension);
        if (entity instanceof EntityPlayer) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            if (!player.worldObj.isRemote) {
                ReflectionHelper.setPrivateValue(EntityPlayerMP.class, player, true, "invulnerableDimensionChange", "field_184851_cj");
                newWorld.getMinecraftServer().getPlayerList().transferPlayerToDimension(player, dimension, new HFTeleporter(newWorld, spawn));
                player.setPositionAndUpdate(spawn.getX(), spawn.getY(), spawn.getZ());
                player.worldObj.updateEntityWithOptionalForce(player, false);
                player.connection.sendPacket(new SPacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()));
            }
        } else if (!entity.worldObj.isRemote) {
            NBTTagCompound tag = new NBTTagCompound();
            entity.writeToNBTOptional(tag);
            entity.setDead();
            Entity teleportedEntity = EntityList.createEntityFromNBT(tag, newWorld);
            if (teleportedEntity != null) {
                teleportedEntity.setPositionAndUpdate(spawn.getX(), spawn.getY(), spawn.getZ());
                teleportedEntity.forceSpawn = true;
                newWorld.spawnEntityInWorld(teleportedEntity);
                teleportedEntity.setWorld(newWorld);
                teleportedEntity.timeUntilPortal = teleportedEntity instanceof EntityPlayer ? 150 : 20;
            }

            oldWorld.resetUpdateEntityTick();
            newWorld.resetUpdateEntityTick();
        }

        entity.timeUntilPortal = entity instanceof EntityLiving ? 150 : 20;
        return true;
    }
}