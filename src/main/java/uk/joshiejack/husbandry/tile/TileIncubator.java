package uk.joshiejack.husbandry.tile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.block.BlockIncubator;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.machine.TileMachineSimple;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TerrainHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@SuppressWarnings("unused")
@PenguinLoader("incubator")
public class TileIncubator extends TileMachineSimple {
    private static final ResourceLocation NULL = new ResourceLocation("");
    public static final HolderRegistry<ResourceLocation> ITEM_REGISTRY = new HolderRegistry<>(NULL);
    public static final HolderRegistry<BlockIncubator.Fill> FILL_REGISTRY = new HolderRegistry<>(BlockIncubator.Fill.EMPTY);
    private long operationTime;

    public TileIncubator() {
        super("week");
    }

    @Override
    public void startMachine(EntityPlayer player) {
        ItemStack egg = handler.getStackInSlot(0);
        int days = 7;
        if (egg.hasTagCompound() && Objects.requireNonNull(egg.getTagCompound()).hasKey("HatchTime")) {
            days = egg.getTagCompound().getInteger("HatchTime");
        }

        operationTime = TimeHelper.TICKS_PER_DAY * days; //time_unit > day
        super.startMachine(player);
    }

    @Override
    public long getOperationalTime() {
        return operationTime;
    }

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return ITEM_REGISTRY.getValue(stack) != NULL;
    }

    public BlockIncubator.Fill getFill() {
        return FILL_REGISTRY.getValue(handler.getStackInSlot(0));
    }

    @Override
    public void finishMachine() {
        ItemStack egg = handler.getStackInSlot(0);
        int hearts = 1;
        if (egg.hasTagCompound() && Objects.requireNonNull(egg.getTagCompound()).hasKey("HeartLevel")) {
            hearts = egg.getTagCompound().getInteger("HeartLevel");
        }

        int offspring = 1 + AnimalStats.getProductSize(world.rand, hearts * 3000);
        for (int j = 0; j < offspring; j++) {
            Entity entity = EntityList.createEntityByIDFromName(ITEM_REGISTRY.getValue(egg), world);
            if (entity != null) {
                if (entity instanceof EntityAgeable) {
                    ((EntityAgeable)entity).setGrowingAge(-Integer.MAX_VALUE);
                }

                if (entity instanceof EntityZombie) {
                    ((EntityZombie)entity).setChild(true);
                }

                if (entity instanceof EntitySlime) {
                    try {
                        Method m = ObfuscationReflectionHelper.findMethod(EntitySlime.class, "func_70799_a", void.class, int.class, boolean.class);
                               // EntitySlime.class.getDeclaredMethod("setSlimeSize", int.class, boolean.class); //TODO: CHECK
                        m.invoke(entity, 1, true);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

                BlockPos target = TerrainHelper.getRandomBlockInRadius(world, pos, 2, World::isAirBlock);
                if (target == null) continue;
                entity.setLocationAndAngles(target.getX(), target.getY(), target.getZ(), 0.0F, 0.0F);

                AnimalStats<?> stats = AnimalStats.getStats(entity);
                if (stats != null) {
                    stats.increaseHappiness(hearts * 3000);
                }

                world.spawnEntity(entity);
            }
        }

        handler.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        operationTime = nbt.getLong("OperationTime");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("OperationTime", operationTime);
        return super.writeToNBT(nbt);
    }
}
