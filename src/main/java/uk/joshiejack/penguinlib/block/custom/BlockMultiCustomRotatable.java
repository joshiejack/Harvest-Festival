package uk.joshiejack.penguinlib.block.custom;

import uk.joshiejack.penguinlib.block.base.BlockMultiRotatable;
import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockMultiCustomRotatable extends BlockMultiCustom {
    public BlockMultiCustomRotatable(ResourceLocation registry, AbstractCustomBlockData defaults, AbstractCustomBlockData... data) {
        super(registry, defaults, data);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, BlockMultiRotatable.FACING);
        return new BlockStateContainer(this, property, BlockMultiRotatable.FACING);
    }

    public IBlockState withFacing(IBlockState state, EnumFacing facing) {
        return state.withProperty(BlockMultiRotatable.FACING, facing);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
        return state.withProperty(BlockMultiRotatable.FACING, rot.rotate(state.getValue(BlockMultiRotatable.FACING)));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState withMirror(@Nonnull IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(BlockMultiRotatable.FACING)));
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(property, getDataFromMeta(meta)).withProperty(BlockMultiRotatable.FACING, getFacingFromMeta(meta));
    }

    @Override
    public AbstractCustomBlockData getDataFromMeta(int meta) {
        return super.getDataFromMeta(meta % data.length);
    }

    private EnumFacing getFacingFromMeta(int meta) {
        return EnumFacing.byHorizontalIndex(meta / data.length);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int enumValue = ids.get(state.getValue(property));
        int faceValue = (state.getValue(BlockMultiRotatable.FACING).getHorizontalIndex());
        return enumValue + (faceValue * data.length);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return ids.get(state.getValue(property)); //Return without the extra data
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = EntityHelper.getFacingFromEntity(placer);
        world.setBlockState(pos, state.withProperty(BlockMultiRotatable.FACING, facing));
    }
}
