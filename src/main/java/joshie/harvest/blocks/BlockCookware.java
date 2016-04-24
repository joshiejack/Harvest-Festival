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
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ITickable;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static joshie.harvest.blocks.BlockCookware.Cookware.*;

public class BlockCookware extends BlockHFBaseMeta<Cookware> {
    public static enum Cookware implements IStringSerializable {
        FRIDGE_TOP, FRIDGE, COUNTER, POT, FRYING_PAN, MIXER, OVEN;

        @Override
        public String getName() {
            return toString();
        }
    }

    public BlockCookware() {
        super(Material.piston, HFTab.tabCooking, Cookware.class);
        setHardness(2.5F);
    }

    @Override
    public String getToolType(Cookware cookware) {
        return cookware == COUNTER ? "axe" : "pickaxe";
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
        Cookware cookware = getEnumFromBlockPos(world, pos);
        switch (cookware) {
            case FRYING_PAN:
                setBlockBounds(0F, 0F, 0F, 1F, 0.25F, 1F);
                break;
            case POT:
                setBlockBounds(0F, 0F, 0F, 1F, 0.75F, 1F);
                break;
            case FRIDGE:
                setBlockBounds(0F, 0F, 0F, 1F, 2F, 1F);
                break;
            case FRIDGE_TOP:
                setBlockBounds(0F, -1F, 0F, 1F, 1F, 1F);
                break;
            default:
                setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
                break;
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        Cookware cookware = getEnumFromState(state);
        if (player.isSneaking()) return false;
        else if (cookware == FRIDGE || cookware == FRIDGE_TOP) {
            player.openGui(HarvestFestival.instance, GuiHandler.FRIDGE, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        } else if (cookware == COUNTER) {
            ItemStack held = player.getCurrentEquippedItem();
            TileEntity tile = null;
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
            ItemStack held = player.getCurrentEquippedItem();
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
        Cookware cookware = getEnumFromBlockPos(world, pos);
        if (cookware == FRIDGE_TOP) {
            world.setBlockToAir(pos.down());
        } else if (cookware == FRIDGE) {
            world.setBlockToAir(pos.up());
        }
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
        return cookware == FRIDGE_TOP ? false : true;
    }
}
