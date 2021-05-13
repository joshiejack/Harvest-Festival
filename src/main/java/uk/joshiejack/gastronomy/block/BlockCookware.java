package uk.joshiejack.gastronomy.block;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.cooking.Cooker;
import uk.joshiejack.gastronomy.tile.*;
import uk.joshiejack.gastronomy.tile.base.TileCookingHeatable;
import uk.joshiejack.penguinlib.block.base.BlockMultiTileRotatable;
import uk.joshiejack.penguinlib.tile.inventory.TileInventory;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;
import static uk.joshiejack.gastronomy.block.BlockCookware.Cookware.*;
import static net.minecraft.util.EnumFacing.*;

public class BlockCookware extends BlockMultiTileRotatable<BlockCookware.Cookware> {
    private static final AxisAlignedBB FRYING_PAN_AABB = new AxisAlignedBB(0.2F, 0F, 0.2F, 0.8F, 0.15F, 0.8F);
    private static final AxisAlignedBB MIXER_AABB = new AxisAlignedBB(0.275F, 0F, 0.275F, 0.725F, 0.725F, 0.725F);
    private static final AxisAlignedBB POT_AABB = new AxisAlignedBB(0.2F, 0F, 0.2F, 0.8F, 0.375F, 0.8F);

    public BlockCookware() {
        super(new ResourceLocation(MODID, "cookware"), Material.PISTON, Cookware.class);
        setHardness(2.5F);
        setSoundType(SoundType.METAL);
        setCreativeTab(Gastronomy.TAB);
    }

    @Override
    public String getToolType(Cookware cookware) {
        switch (cookware) {
            case COUNTER:
            case COUNTER_IC:
            case COUNTER_OC:
            case ISLAND:
            case ISLAND_IC:
            case ISLAND_OC:
            case SINK:
                return "axe";
            default:
                return "pickaxe";
        }
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public Material getMaterial(@Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case COUNTER:
            case COUNTER_IC:
            case COUNTER_OC:
            case OVEN_OFF:
            case OVEN_ON:
            case ISLAND:
            case ISLAND_IC:
            case ISLAND_OC:
            case SINK:
                return Material.WOOD;
            default:
                return material;
        }
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public SoundType getSoundType(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nullable Entity entity) {
        switch (getEnumFromState(state)) {
            case COUNTER:
            case COUNTER_IC:
            case COUNTER_OC:
            case ISLAND:
            case ISLAND_IC:
            case ISLAND_OC:
            case SINK:
                return SoundType.WOOD;
            default:
                return getSoundType();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        return (getEnumFromState(state) == MIXER ? layer == BlockRenderLayer.TRANSLUCENT : layer == BlockRenderLayer.CUTOUT_MIPPED);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        Cookware cookware = getEnumFromState(state);
        switch (cookware) {
            case FRYING_PAN:
                return FRYING_PAN_AABB;
            case POT:
                return POT_AABB;
            case MIXER:
                return MIXER_AABB;
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState ret = super.getActualState(state, world, pos);
        Cookware cookware = getEnumFromState(ret);
        if (cookware == OVEN_OFF || cookware == OVEN_ON) {
            TileEntity tile = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos.up(), Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos.up());
            if (tile instanceof TileCookingHeatable) {
                if (((TileCookingHeatable)tile).isCooking()) {
                    return ret.withProperty(property, OVEN_ON);
                }
            }

            return ret.withProperty(property, OVEN_OFF);
        } else if (cookware.getName().contains("counter") || cookware.getName().contains("island")) {
            EnumFacing northFacing = getFacing(NORTH, world, pos);
            EnumFacing eastFacing = getFacing(EAST, world, pos);
            EnumFacing southFacing = getFacing(SOUTH, world, pos);
            EnumFacing westFacing = getFacing(WEST, world, pos);

            //Inner Corner
            Cookware inner = cookware.getName().contains("counter") ? COUNTER_IC: ISLAND_IC;
            if (northFacing == WEST && westFacing == NORTH) return state.withProperty(property, inner).withProperty(FACING, WEST);
            if (southFacing == WEST && westFacing == SOUTH) return state.withProperty(property, inner).withProperty(FACING, SOUTH);
            if (southFacing == EAST && eastFacing == SOUTH) return state.withProperty(property, inner).withProperty(FACING, EAST);
            if (northFacing == EAST && eastFacing == NORTH) return state.withProperty(property, inner).withProperty(FACING, NORTH);

            //Outer Corner
            Cookware outer = cookware.getName().contains("counter") ? COUNTER_OC: ISLAND_OC;
            if (northFacing == EAST && westFacing == SOUTH) return state.withProperty(property, outer).withProperty(FACING, EAST);
            if (southFacing == EAST && westFacing == NORTH) return state.withProperty(property, outer).withProperty(FACING, NORTH);
            if (southFacing == WEST && eastFacing == NORTH) return state.withProperty(property, outer).withProperty(FACING, WEST);
            if (northFacing == WEST && eastFacing == SOUTH) return state.withProperty(property, outer).withProperty(FACING, SOUTH);
        }

        return ret;
    }

    private EnumFacing getFacing(EnumFacing facing, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos.offset(facing));
        if (tile instanceof TileCounter || tile instanceof TileOven || tile instanceof TileFridge || tile instanceof TileSink) {
            return ((Rotatable)tile).getFacing();
        }

        return EnumFacing.DOWN;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileInventory) {
            dropInventory((TileInventory) tile, world, pos);
            world.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(world, pos, state);
    }

    private void dropInventory(TileInventory inventory, World world, BlockPos pos) {
        for (int i = 0; i < inventory.getHandler().getSlots(); i++) {
            ItemStack itemstack = inventory.getHandler().getStackInSlot(i);
            if (!itemstack.isEmpty()) {
                Cooker.removeUtensilTag(itemstack);
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemstack);
            }
        }
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case FRYING_PAN:        return new TileFryingPan();
            case MIXER:             return new TileMixer();
            case OVEN_OFF:
            case OVEN_ON:           return new TileOven();
            case POT:               return new TilePot();
            case SINK:              return new TileSink();
            default:                return new TileCounter();
        }
    }

    @Nonnull
    protected ItemStack getCreativeStack(Cookware cookware) {
        switch (cookware) {
            case ISLAND_OC:
            case ISLAND_IC:
            case COUNTER_IC:
            case COUNTER_OC:
            case OVEN_ON:           return ItemStack.EMPTY;
            default:                return super.getCreativeStack(cookware);
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCounter) {
            return  ((TileCounter)tile).getFacing() == face ? BlockFaceShape.CENTER : BlockFaceShape.SOLID;
        } else return super.getBlockFaceShape(world, state, pos, face);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    @Override
    protected void registerModel(Item item, Cookware cookware) {
        if (cookware == OVEN_OFF || cookware == OVEN_ON) {
            ModelLoader.setCustomModelResourceLocation(item, cookware.ordinal(), new ModelResourceLocation(getRegistryName(), "inventory_oven"));
        } else super.registerModel(item, cookware);
    }

    public enum Cookware implements IStringSerializable {
        COUNTER, COUNTER_IC, COUNTER_OC, FRYING_PAN, MIXER, OVEN_OFF, OVEN_ON, POT,
        ISLAND, ISLAND_IC, ISLAND_OC, SINK;

        @Override
        public @Nonnull String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
