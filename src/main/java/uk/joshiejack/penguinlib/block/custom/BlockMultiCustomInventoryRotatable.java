package uk.joshiejack.penguinlib.block.custom;

import uk.joshiejack.penguinlib.data.custom.block.AbstractCustomBlockData;
import uk.joshiejack.penguinlib.data.custom.block.CustomBlockInventoryData;
import uk.joshiejack.penguinlib.tile.custom.TileCustomInventoryRotatable;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
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

public class BlockMultiCustomInventoryRotatable extends BlockMultiCustomInventory {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockMultiCustomInventoryRotatable(ResourceLocation registry, CustomBlockInventoryData defaults, CustomBlockInventoryData... data) {
        super(registry, defaults, data);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileCustomInventoryRotatable().withData(state, this);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if(property == null) return new BlockStateContainer(this, temporary, FACING);
        return new BlockStateContainer(this, property, FACING);
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState withRotation(@Nonnull IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof Rotatable) {
            ((Rotatable) tile).setFacing(EntityHelper.getFacingFromEntity(entity));
        }
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (tile instanceof Rotatable) {
            return state.withProperty(FACING, ((Rotatable)tile).getFacing());
        }

        return state;
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    protected void registerModel(Item item, AbstractCustomBlockData cd) {
        if (Character.toLowerCase(property.getName().charAt(0)) < Character.toLowerCase('f')) {
            ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), new ModelResourceLocation(getRegistryName(), property.getName() + "=" + cd.name + ",facing=north"));
        } else {
            ModelLoader.setCustomModelResourceLocation(item, ids.get(cd), new ModelResourceLocation(getRegistryName(), "facing=north," + property.getName() + "=" + cd.name));
        }
    }
}
