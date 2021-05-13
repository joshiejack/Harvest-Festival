package uk.joshiejack.husbandry.entity.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.husbandry.block.HusbandryBlocks;
import uk.joshiejack.husbandry.tile.TileNest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.EntityCreature;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Predicate;

import static uk.joshiejack.husbandry.entity.ai.EntityAITimerBase.Orientation.ABOVE;

public class EntityAILayEgg extends EntityAITimerBase {
    private static final Predicate<IBlockState> IS_NEST = BlockStateMatcher.forBlock(HusbandryBlocks.TRAY).where(HusbandryBlocks.TRAY.property, tray -> Objects.equals(tray, BlockTray.Tray.NEST_EMPTY));
    private int eggTimer;

    public EntityAILayEgg(EntityCreature entity, AnimalStats<?> stats) {
        super(entity, stats, ABOVE, 8);
    }

    @Override
    public boolean shouldExecute() {
        return stats.canProduceProduct() && entity.getRNG().nextInt(5) == 0 && super.shouldExecute();
    }

    @Override
    protected boolean shouldMoveTo(World world, @Nonnull BlockPos pos) {
        return IS_NEST.test(world.getBlockState(pos));
    }

    @Override
    public void updateTask() {
        super.updateTask();
        entity.getLookHelper().setLookPosition((double) destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1,
                (double) destinationBlock.getZ() + 0.5D, 10.0F, (float) entity.getVerticalFaceSpeed());

        if (isNearDestination()) {
            eggTimer++;

            if (eggTimer > 60) {
                TileEntity tile = entity.world.getTileEntity(destinationBlock);
                if (tile instanceof TileNest) {
                    TileNest nest = ((TileNest) tile);
                    if (nest.getHandler().getStackInSlot(0).isEmpty()) {
                        ItemStack product = stats.getProduct();
                        NBTTagCompound data = new NBTTagCompound();
                        data.setInteger("HatchTime", stats.getType().getDaysToBirth());
                        data.setInteger("HeartLevel", stats.getHearts());
                        product.setTagCompound(data);
                        nest.getHandler().setStackInSlot(0, product);
                        stats.setProduced(1);
                        entity.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (entity.getRNG().nextFloat() - entity.getRNG().nextFloat()) * 0.2F + 1.0F);
                        timeoutCounter = 9999;
                    }
                }

                eggTimer = 0;
            }
        }
    }
}
