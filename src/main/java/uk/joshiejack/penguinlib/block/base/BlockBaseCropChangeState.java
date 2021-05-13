package uk.joshiejack.penguinlib.block.base;

import uk.joshiejack.penguinlib.util.helpers.minecraft.FakePlayerHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.Random;

public abstract class BlockBaseCropChangeState extends BlockBaseCrop {
    private final IBlockState finalState;
    private final ItemStack drop;

    public BlockBaseCropChangeState(ResourceLocation registry, IBlockState finalState, ItemStack drop, int stages) {
        super(registry, stages);
        this.finalState = finalState;
        this.drop = drop;
    }

    @Override
    protected ItemStack getCropStack() {
        return drop;
    }

    @Override
    public void updateTick(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        checkAndDropBlock(worldIn, pos, state);
        if (!worldIn.isAreaLoaded(pos, 1))
            return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            int i = getAge(state);

            if (i < getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);
                if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / f) + 1) == 0)) {
                    int j = i + 1;
                    setState(worldIn, pos, (j >= getMaxAge() ? getFinalState() : withAge(j)));
                    ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }

    @Override
    public void grow(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        int i = getAge(state) + getBonemealAgeIncrease(world);
        int j = getMaxAge();

        if (i > j) {
            i = j;
        }

        setState(world, pos, (i > getMaxAge() ? getFinalState() : withAge(i)));
    }

    public IBlockState getFinalState() {
        return finalState;
    }

    private void setState(World world, BlockPos target, IBlockState state) {
        IBlockState down = world.getBlockState(target.down());
        boolean iplantable = state.getBlock() instanceof IPlantable;
        if (!iplantable || down.getBlock().canSustainPlant(down, world, target.down(), EnumFacing.UP, (IPlantable) state.getBlock())) {
            world.setBlockState(target, state);
            state.getBlock().onBlockPlacedBy(world, target, state, FakePlayerHelper.getFakePlayerWithPosition((WorldServer) world, target), state.getBlock().getItem(world, target, state));
        }
    }
}
