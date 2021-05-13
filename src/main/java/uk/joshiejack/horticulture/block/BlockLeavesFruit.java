package uk.joshiejack.horticulture.block;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.HorticultureConfig;
import uk.joshiejack.penguinlib.block.base.BlockMultiLeaves;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("WeakerAccess")
public abstract class BlockLeavesFruit<E extends Enum<E> & IStringSerializable> extends BlockMultiLeaves<E> implements IGrowable {
    public BlockLeavesFruit(ResourceLocation registry, Class<E> clazz) {
        super(registry, clazz);
        setCreativeTab(Horticulture.TAB);
    }

    @Override
    @Nonnull
    public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune)  {
        return Item.getItemFromBlock(HorticultureBlocks.SAPLING);
    }

    protected abstract boolean isValidLocationForFruit(World world, BlockPos pos);

    @Nullable
    protected abstract IBlockState getFruitState(E e);

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand)  {
        super.updateTick(world, pos, state, rand);
        if (HorticultureConfig.leavesGenerateFruit && world.rand.nextDouble() <= HorticultureConfig.fruitGrowthChance) {
            grow(world, rand, pos, state);
        }
    }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return false;
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (state.getValue(DECAYABLE) && isValidLocationForFruit(worldIn, pos.down()) && worldIn.isAirBlock(pos.down()) && worldIn.isAirBlock(pos.down(2))) {
            IBlockState fruitState = getFruitState(getEnumFromState(state));
            if (fruitState != null) worldIn.setBlockState(pos.down(), fruitState);
        }
    }
}