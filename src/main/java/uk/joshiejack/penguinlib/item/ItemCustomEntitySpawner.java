package uk.joshiejack.penguinlib.item;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.data.custom.entity.AbstractEntityData;
import uk.joshiejack.penguinlib.item.base.ItemMultiMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemCustomEntitySpawner extends ItemMultiMap<AbstractEntityData<Entity>> {
    public ItemCustomEntitySpawner() {
        super(new ResourceLocation(PenguinLib.MOD_ID, "custom_entity_spawner"), AbstractEntityData.DATA_MAP);
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected AbstractEntityData<Entity> getNullEntry() {
        for (AbstractEntityData<Entity> data : AbstractEntityData.DATA_MAP.values()) return data;
        return AbstractEntityData.EMPTY; //"Shouldn't happen"
    }

    @Nullable
    private static Entity newEntity(@Nullable Class<? extends Entity> clazz, World worldIn, ResourceLocation registryName) {
        if (clazz == null) {
            return null;
        } else {
            try {
                net.minecraftforge.fml.common.registry.EntityEntry entry = net.minecraftforge.fml.common.registry.EntityRegistry.getEntry(clazz);
                if (entry != null) return entry.newInstance(worldIn);
                return clazz.getConstructor(World.class, ResourceLocation.class).newInstance(worldIn, registryName);
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        AbstractEntityData<? extends Entity> data = getObjectFromStack(stack);
        if (data != null) {
            if (!world.isRemote) {
                Entity entity = newEntity(data.entity, world, data.registryName);
                if (entity instanceof EntityLiving) {
                    double x = pos.getX() + 0.5;
                    double y = pos.up().getY();
                    double z = pos.getZ() + 0.5;
                    EntityLiving entityliving = (EntityLiving) entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityliving)), null);
                    world.spawnEntity(entity);
                    entityliving.playLivingSound();
                }
            }

            stack.splitStack(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
