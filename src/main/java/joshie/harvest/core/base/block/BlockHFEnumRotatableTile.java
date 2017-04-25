package joshie.harvest.core.base.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.IFaceable;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class BlockHFEnumRotatableTile<B extends BlockHFEnumRotatableTile, E extends Enum<E> & IStringSerializable> extends BlockHFEnum<B, E> {
    protected static final PropertyDirection FACING = BlockHorizontal.FACING;

    //Main Constructor
    public BlockHFEnumRotatableTile(Material material, Class<E> clazz, CreativeTabs tab) {
        super(material, clazz, tab);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public BlockHFEnumRotatableTile(Material material, Class<E> clazz) {
        this(material, clazz, HFTab.FARMING);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING);
        return new BlockStateContainer(this, property, FACING);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, @Nonnull ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            ((IFaceable) tile).setFacing(EntityHelper.getFacingFromEntity(entity));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            return state.withProperty(FACING, ((IFaceable)tile).getFacing());
        }

        return state;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.TROUGH;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item, String name) {
        for (int i = 0; i < values.length; i++) {
            if (Character.toLowerCase(property.getName().charAt(0)) < Character.toLowerCase('f')) {
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), property.getName() + "=" + getEnumFromMeta(i).getName() + ",facing=north"));
            } else {
                ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(getRegistryName(), "facing=north," + property.getName() + "=" + getEnumFromMeta(i).getName()));
            }

        }
    }
}