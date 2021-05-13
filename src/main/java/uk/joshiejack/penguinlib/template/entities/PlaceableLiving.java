package uk.joshiejack.penguinlib.template.entities;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@PenguinLoader("living")
public class PlaceableLiving extends PlaceableEntity {
    public static Map<ResourceLocation, CustomEntitySpawning> CUSTOM_SPAWN_HANDLERS = Maps.newHashMap();
    private ResourceLocation entityName;
    private NBTTagCompound data;

    public PlaceableLiving() {}
    public PlaceableLiving(ResourceLocation key, NBTTagCompound nbtTagCompound, BlockPos position) {
        super(position);
        this.entityName = key;
        this.data = nbtTagCompound;
    }

    public ResourceLocation getEntityName() {
        return entityName;
    }

    public NBTTagCompound getTag() {
        return data;
    }

    @Override
    public Class<? extends Entity> getEntityClass() {
        return EntityList.getClass(entityName);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public Entity getEntity(World world, BlockPos pos, Rotation rotation) {
        if (CUSTOM_SPAWN_HANDLERS.containsKey(entityName)) return CUSTOM_SPAWN_HANDLERS.get(entityName).getEntity(world, pos, rotation, data);
        Entity entity = EntityList.createEntityByIDFromName(entityName, world);
        try {
            Method m = Entity.class.getDeclaredMethod("readEntityFromNBT", NBTTagCompound.class);
            m.setAccessible(true);
            m.invoke(entity, data);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        boolean solid = entity.world.getBlockState(pos.down()).isSideSolid(entity.world, pos, EnumFacing.UP);
        entity.setPosition(pos.getX() + 0.5,  pos.getY() + (solid ? 0 : 0.5), pos.getZ() + 0.5);
        return entity;
    }

    @Override
    public PlaceableEntity getCopyFromEntity(Entity entity, BlockPos position) {
        NBTTagCompound tag = new NBTTagCompound();
        try {
            Method m = Entity.class.getDeclaredMethod("writeEntityToNBT", NBTTagCompound.class);
            m.setAccessible(true);
            m.invoke(entity, tag);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return new PlaceableLiving(EntityList.getKey(entity), tag, position);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.MOVEIN;
    }
}
