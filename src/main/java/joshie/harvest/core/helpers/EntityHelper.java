package joshie.harvest.core.helpers;

import joshie.harvest.core.util.HFTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EntityHelper {
    //Loops through all the animals in the specified dimension id
    @SuppressWarnings("unchecked")
    public static <E extends Entity> E getAnimalFromUUID(World world, UUID uuid) {
        for (int i = 0; i < world.loadedEntityList.size(); i++) {
            Entity entity = world.loadedEntityList.get(i);
            if (entity instanceof EntityAnimal) {
                if (getEntityUUID(entity).equals(uuid)) {
                    return (E) entity;
                }
            }
        }

        return null;
    }

    /** Gets the player from the uuid **/
    public static EntityPlayerMP getPlayerFromUUID(UUID uuid) {
        //Loops through every single player
        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList()) {
            if (getPlayerUUID(player).equals(uuid)) {
                return (EntityPlayerMP) player;
            }
        }

        return null;
    }

    public static boolean isFakePlayer(EntityPlayer player) {
        return player instanceof FakePlayer || player.getGameProfile().getName().equals("CoFH") || player.getGameProfile().getName().startsWith("[Thaumcraft");
    }

    public static <T extends Entity> List<T> getEntities(Class<? extends T> t, World world, BlockPos pos, double size) {
        return world.getEntitiesWithinAABB(t, new AxisAlignedBB(pos.getX() - 0.5F, pos.getY() - 0.5F, pos.getZ() - 0.5F, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F).expand(size, size, size));
    }

    public static boolean teleport(Entity entity, int dimension, BlockPos spawn) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer oldWorld = server.worldServerForDimension(entity.getEntityWorld().provider.getDimension());
        WorldServer newWorld = server.worldServerForDimension(dimension);
        if (oldWorld != newWorld) {
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
        }

        if (entity instanceof EntityPlayerMP) {
            ReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP) entity, true, "invulnerableDimensionChange", "field_184851_cj");
            entity.setPositionAndUpdate(spawn.getX() + 0.5D, spawn.getY() + 0.1D, spawn.getZ() + 0.5D);
        }

        entity.timeUntilPortal = entity instanceof EntityLiving ? 150 : 20;
        return true;
    }

    public static EnumFacing getFacingFromEntity(EntityLivingBase entity) {
        int facing = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        EnumFacing dir = EnumFacing.NORTH;
        if (facing == 0) return EnumFacing.NORTH;
        if (facing == 1) return EnumFacing.EAST;
        if (facing == 2) return EnumFacing.SOUTH;
        if (facing == 3) return EnumFacing.WEST;
        return dir;
    }

    public static UUID getLastKnownUUID(EntityPlayer player) {
        String name = player.getGameProfile().getName();
        for (Map.Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }

        return getPlayerUUID(player);
    }

    public static UUID getEntityUUID(Entity entity) {
        return entity.getPersistentID();
    }

    public static UUID getPlayerUUID(EntityPlayer player) {
        return EntityPlayer.getUUID(player.getGameProfile());
    }

    public static EntityPlayer getPlayerFromSource(DamageSource source) {
        if (source.getEntity() instanceof EntityPlayer) return ((EntityPlayer)source.getEntity());
        else if (source.getSourceOfDamage() instanceof EntityPlayer) return ((EntityPlayer)source.getSourceOfDamage());
        else return null;
    }
}