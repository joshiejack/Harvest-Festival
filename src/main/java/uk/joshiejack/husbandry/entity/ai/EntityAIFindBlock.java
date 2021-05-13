package uk.joshiejack.husbandry.entity.ai;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.BlockStates;
import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

public class EntityAIFindBlock extends EntityAITimerBase implements IPlantable {
    public EntityAIFindBlock(EntityCreature entity, AnimalStats<?> stats) {
        super(entity, stats, Orientation.BESIDE, 16);
    }

    @Override
    public boolean shouldExecute() {
        return stats.canProduceProduct() && entity.getRNG().nextInt(5) == 0 && super.shouldExecute();
    }

    @Override
    protected boolean shouldMoveTo(World world, @Nonnull BlockPos pos) {
        IBlockState below = world.getBlockState(pos.down());
        return below.getBlock().canSustainPlant(below, entity.world, pos.down(), EnumFacing.UP, this);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        entity.getLookHelper().setLookPosition((double) destinationBlock.getX() + 0.5D, destinationBlock.getY() + 1,
                (double) destinationBlock.getZ() + 0.5D, 10.0F, (float) entity.getVerticalFaceSpeed());

        if (isNearDestination()) {
            ItemStack stack = stats.getProduct();
            if (!stack.isEmpty()) {
                IBlockState state = ((ItemBlock) stack.getItem()).getBlock()
                        .getStateForPlacement(entity.world, destinationBlock, EnumFacing.UP, 0F, 0F, 0F, stack.getItemDamage(),
                                FakePlayerHelper.getFakePlayerWithPosition((WorldServer) entity.world, destinationBlock), EnumHand.MAIN_HAND);
                entity.world.setBlockState(destinationBlock, state, 2);
                stats.setProduced(1);
            }

            timeoutCounter = 9999;
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return BlockStates.TALL_GRASS;
    }
}
