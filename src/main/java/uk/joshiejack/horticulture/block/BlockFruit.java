package uk.joshiejack.horticulture.block;

import uk.joshiejack.horticulture.Horticulture;
import uk.joshiejack.horticulture.HorticultureConfig;
import uk.joshiejack.horticulture.item.HorticultureItems;
import uk.joshiejack.horticulture.item.ItemCrop;
import uk.joshiejack.penguinlib.block.base.BlockMultiGrowableBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Random;

import static uk.joshiejack.horticulture.Horticulture.MODID;

public class BlockFruit extends BlockMultiGrowableBush<BlockFruit.Fruit> implements IGrowable {
    private static final AxisAlignedBB SML_AAB = new AxisAlignedBB(0.30000001192092896D, 0.5D, 0.30000001192092896D, 0.699999988079071D, 1D, 0.699999988079071D);
    private static final AxisAlignedBB MED_AABB = new AxisAlignedBB(0.30000001192092896D, 0.4D, 0.30000001192092896D, 0.699999988079071D, 1D, 0.699999988079071D);
    private static final AxisAlignedBB GROWN_AABB = new AxisAlignedBB(0.30000001192092896D, 0.2D, 0.30000001192092896D, 0.699999988079071D, 1D, 0.699999988079071D);

    public BlockFruit() {
        super(new ResourceLocation(MODID, "fruit"), Fruit.class);
        setCreativeTab(Horticulture.TAB);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        switch (state.getValue(STAGE)) {
            case 0:
                return SML_AAB;
            case 1:
                return MED_AABB;
            case 2:
            case 3:
                return GROWN_AABB;
        }

        return GROWN_AABB;
    }

    @Override
    public boolean canBlockStay(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state)  {
        if (state.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        {
            IBlockState leaves = world.getBlockState(pos.up());
            return leaves.getBlock().isLeaves(leaves, world,pos.up());
        }

        return this.canSustainBush(world.getBlockState(pos.down()));
    }

    private ItemStack getStackFromState(IBlockState state) {
        int stage = state.getValue(STAGE);
        if (stage == 3) {
            Fruit fruit = getEnumFromState(state);
            switch (fruit) {
                case APPLE:
                    return new ItemStack(Items.APPLE);
                case ORANGE:
                    return HorticultureItems.CROP.getStackFromEnum(ItemCrop.Crops.ORANGE);
                case PEACH:
                    return HorticultureItems.CROP.getStackFromEnum(ItemCrop.Crops.PEACH);
                case BANANA:
                    return HorticultureItems.CROP.getStackFromEnum(ItemCrop.Crops.BANANA);
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player,
                                    @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        if (state.getValue(STAGE) == 3) {
            dropBlockAsItem(world, pos, state, 0);
            return world.setBlockToAir(pos);
        } else return false;
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        ItemStack drop = getStackFromState(state);
        if (!drop.isEmpty()) drops.add(drop);
    }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
        if(worldIn.rand.nextDouble() <= HorticultureConfig.fruitGrowthChance) {
            super.updateTick(worldIn, pos, state, rand);
        }
    }

    @Nonnull
    protected ItemStack getCreativeStack(Fruit object) {
        return ItemStack.EMPTY;
    }

    public enum Fruit implements IStringSerializable {
        APPLE, BANANA, ORANGE, PEACH;

        @Override
        public @Nonnull String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}
