package joshie.harvest.blocks;

import joshie.harvest.HarvestFestival;
import joshie.harvest.blocks.BlockCookware.Cookware;
import joshie.harvest.blocks.tiles.*;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.util.base.BlockHFBaseEnumRotatableTile;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

public class BlockCookware extends BlockHFBaseEnumRotatableTile<Cookware> {
    private static final AxisAlignedBB FRYING_PAN_AABB = new AxisAlignedBB(0.2F, 0F, 0.2F, 0.8F, 0.15F, 0.8F);
    private static final AxisAlignedBB MIXER_AABB = new AxisAlignedBB(0.275F, 0F, 0.275F, 0.725F, 0.725F, 0.725F);
    private static final AxisAlignedBB POT_AABB = new AxisAlignedBB(0.2F, 0F, 0.2F, 0.8F, 0.375F, 0.8F);
    private static Item cookware = null;

    public enum Cookware implements IStringSerializable {
        FRIDGE_TOP(false), FRIDGE(true), COUNTER(true), POT(true), FRYING_PAN(true), MIXER(true), OVEN_OFF(true), OVEN_ON(false), COUNTER_IC(false), COUNTER_OC(false);

        private final boolean isReal;
        Cookware(boolean isReal) {
            this.isReal = isReal;
        }

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
    @SideOnly(Side.CLIENT)
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        Cookware cookware = getEnumFromState(state); //Yayayayayyayayayyayyyyyyyyyyyyyyyyyyyyyyyyyyaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaayya
        return cookware == FRIDGE_TOP ? false : cookware == MIXER? layer == BlockRenderLayer.TRANSLUCENT : layer == BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public String getToolType(Cookware cookware) {
        return cookware == COUNTER ? "axe" : "pickaxe";
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return getEnumFromState(state) == COUNTER ? Material.WOOD : super.getMaterial(state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        Cookware cookware = getEnumFromState(state);
        switch (cookware) {
            case FRYING_PAN:
                return FRYING_PAN_AABB;
            case POT:
                return POT_AABB;
            case FRIDGE:
                return new AxisAlignedBB(0F, 0F, 0F, 1F, 2F, 1F);
            case FRIDGE_TOP:
                return new AxisAlignedBB(0F, -1F, 0F, 1F, 1F, 1F);
            case MIXER:
                return MIXER_AABB;
            default:
                return FULL_BLOCK_AABB;
        }
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
                ((TileCooking) tile).update();
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
                return true;
            } else if (held != null && !isCookware(held)) {
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

    public static boolean isCookware(ItemStack stack) {
        if (cookware == null) cookware = Item.getItemFromBlock(HFBlocks.COOKWARE);
        return stack.getItem() == cookware;
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
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState ret = super.getActualState(state, world, pos);
        Cookware cookware = getEnumFromState(ret);
        if (cookware == OVEN_OFF || cookware == OVEN_ON) {
            TileEntity tile = world.getTileEntity(pos.up());
            if (tile instanceof TileHeatable) {
                if (((TileHeatable)tile).isCooking()) {
                    return ret.withProperty(property, OVEN_ON);
                }
            }

            return ret.withProperty(property, OVEN_OFF);
        }

        return ret;
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
            case OVEN_OFF:
                return new TileOven();
            case OVEN_ON:
                return new TileOven();
            default:
                return null;
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return 99;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected boolean isValidTab(CreativeTabs tab, Cookware cookware) {
        return cookware.isReal? super.isValidTab(tab, cookware) : false;
    }
}