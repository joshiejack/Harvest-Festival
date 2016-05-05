package joshie.harvest.blocks;

import joshie.harvest.HarvestFestival;
import joshie.harvest.blocks.BlockCookware.Cookware;
import joshie.harvest.blocks.items.ItemBlockCookware;
import joshie.harvest.blocks.tiles.*;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.blocks.BlockCookware.Cookware.*;

public class BlockCookware extends BlockHFBaseMeta<Cookware> {
    public enum Cookware implements IStringSerializable {
        FRIDGE_TOP, FRIDGE, COUNTER, POT, FRYING_PAN, MIXER, OVEN;

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }

    public BlockCookware() {
        super(Material.PISTON, Cookware.class, HFTab.COOKING);
        setHardness(2.5F);
        setSoundType(SoundType.METAL);
    }

    @Override
    public String getToolType(Cookware cookware) {
        return cookware == COUNTER ? "axe" : "pickaxe";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Cookware cookware = getEnumFromState(state);
        switch (cookware) {
            case FRYING_PAN:
                new AxisAlignedBB(0F, 0F, 0F, 1F, 0.25F, 1F);
                break;
            case POT:
                new AxisAlignedBB(0F, 0F, 0F, 1F, 0.75F, 1F);
                break;
            case FRIDGE:
                new AxisAlignedBB(0F, 0F, 0F, 1F, 2F, 1F);
                break;
            case FRIDGE_TOP:
                new AxisAlignedBB(0F, -1F, 0F, 1F, 1F, 1F);
                break;
            default:
                new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F);
                break;
        }

        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        Cookware cookware = getEnumFromState(state);
        if (player.isSneaking()) return false;
        else if (cookware == FRIDGE || cookware == FRIDGE_TOP) {
            player.openGui(HarvestFestival.instance, GuiHandler.FRIDGE, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        } else if (cookware == COUNTER) {
            ItemStack held = player.getHeldItem(hand);
            TileEntity tile;
            if (cookware == COUNTER) tile = world.getTileEntity(pos);
            else tile = world.getTileEntity(pos.down());
            if (!(tile instanceof TileCounter)) return false;
            if (cookware == COUNTER && held == null) {
                ((ITickable) tile).update();
            }
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileCooking) {
            TileCooking cooking = (TileCooking) tile;
            ItemStack held = player.getHeldItem(hand);
            if (!cooking.canAddItems()) {
                if (!player.inventory.addItemStackToInventory(cooking.getResult())) {
                    if (!world.isRemote) {
                        ItemHelper.spawnItem(world, pos.getX(), pos.getY() + 1, pos.getZ(), cooking.getResult());
                    }
                }

                cooking.clear();
            } else if (held != null && !(held.getItem() instanceof ItemBlockCookware)) {
                if (cooking.addIngredient(held)) {
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            ((IFaceable) tile).setFacing(DirectionHelper.getFacingFromEntity(entity));
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileFridge) {
            world.setBlockState(pos.up(), getStateFromEnum(FRIDGE_TOP), 2);
        }
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        try {
            Cookware cookware = getEnumFromBlockPos(world, pos);
            if (cookware == FRIDGE_TOP) {
                world.setBlockToAir(pos.down());
            } else if (cookware == FRIDGE) {
                world.setBlockToAir(pos.up());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        Cookware cookware = getEnumFromState(state);
        if (cookware == FRIDGE_TOP) {
            world.setBlockToAir(pos.down());
        } else if (cookware == FRIDGE) {
            world.setBlockToAir(pos.up());
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getEnumFromState(state) == FRIDGE_TOP ? FRIDGE.ordinal() : super.damageDropped(state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return getEnumFromState(state) != FRIDGE_TOP;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        Cookware cookware = getEnumFromState(state);
        switch (cookware) {
            case FRIDGE:
                return new TileFridge();
            case COUNTER:
                return new TileCounter();
            case POT:
                return new TilePot();
            case FRYING_PAN:
                return new TileFryingPan();
            case MIXER:
                return new TileMixer();
            case OVEN:
                return new TileOven();
            default:
                return null;
        }
    }

    @Override
    public boolean isActive(Cookware cookware) {
        return cookware != FRIDGE_TOP;
    }
}