package joshie.harvest.blocks;

import joshie.harvest.HarvestFestival;
import joshie.harvest.blocks.items.ItemBlockCookware;
import joshie.harvest.blocks.tiles.TileCooking;
import joshie.harvest.blocks.tiles.TileFridge;
import joshie.harvest.blocks.tiles.TileFryingPan;
import joshie.harvest.blocks.tiles.TileKitchen;
import joshie.harvest.blocks.tiles.TileMixer;
import joshie.harvest.blocks.tiles.TileOven;
import joshie.harvest.blocks.tiles.TilePot;
import joshie.harvest.blocks.tiles.TileSteamer;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.Cooking;
import joshie.harvest.core.handlers.GuiHandler;
import joshie.harvest.core.helpers.generic.DirectionHelper;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.lib.RenderIds;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCookware extends BlockHFBaseMeta {
    public static final int FRIDGE_TOP = 0;
    public static final int FRIDGE = 1;
    public static final int KITCHEN = 2;
    public static final int POT = 3;
    public static final int FRYING_PAN = 4;
    public static final int MIXER = 5;
    public static final int OVEN = 6;
    public static final int STEAMER = 7;

    public BlockCookware() {
        super(Material.piston, HFTab.tabCooking);
        setHardness(2.5F);
    }

    @Override
    public String getToolType(int meta) {
        return meta == KITCHEN ? "axe" : super.getToolType(meta);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return RenderIds.ALL;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess block, int x, int y, int z) {
        int meta = block.getBlockMetadata(x, y, z);
        switch (meta) {
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        int meta = world.getBlockMetadata(x, y, z);
        if (player.isSneaking()) return false;
        else if (meta == FRIDGE || meta == FRIDGE_TOP) {
            player.openGui(HarvestFestival.instance, GuiHandler.FRIDGE, world, x, y, z);
            return true;
        } else if (meta == KITCHEN) {
            ItemStack held = player.getCurrentEquippedItem();
            TileEntity tile = null;
            if (meta == KITCHEN) tile = world.getTileEntity(x, y, z);
            else tile = world.getTileEntity(x, y - 1, z);
            if (!(tile instanceof TileKitchen)) return false;
            if (meta == KITCHEN && held == null) {
                tile.updateEntity();
            }
        }
        
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileCooking) {
            TileCooking cooking = (TileCooking) tile;
            ItemStack held = player.getCurrentEquippedItem();
            if (!cooking.canAddItems()) {
                if (!player.inventory.addItemStackToInventory(cooking.getResult())) {
                    if (!world.isRemote) {
                        ItemHelper.spawnItem(world, x, y + 1, z, cooking.getResult());
                    }
                }

                cooking.clear();
            } else if (held != null && !(held.getItem() instanceof ItemBlockCookware)) {
                if (cooking.addIngredient(held)) {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    return true;
                }
            }
        }

        return false;

    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IFaceable) {
            ((IFaceable) tile).setFacing(DirectionHelper.getFacingFromEntity(entity));
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileFridge) {
            world.setBlock(x, y + 1, z, this, FRIDGE_TOP, 2);
        }
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == FRIDGE_TOP) {
            world.setBlockToAir(x, y - 1, z);
        } else if (meta == FRIDGE) {
            world.setBlockToAir(x, y + 1, z);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (meta == FRIDGE_TOP) {
            world.setBlockToAir(x, y - 1, z);
        } else if (meta == FRIDGE) {
            world.setBlockToAir(x, y + 1, z);
        }
    }

    @Override
    public int damageDropped(int meta) {
        return meta == FRIDGE_TOP ? FRIDGE : super.damageDropped(meta);
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return meta != FRIDGE_TOP;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        switch (meta) {
            case FRIDGE:
                return new TileFridge();
            case KITCHEN:
                return new TileKitchen();
            case POT:
                return new TilePot();
            case FRYING_PAN:
                return new TileFryingPan();
            case MIXER:
                return new TileMixer();
            case OVEN:
                return new TileOven();
            case STEAMER:
                return new TileSteamer();
            default:
                return null;
        }
    }

    @Override
    public boolean isActive(int meta) {
        return meta == STEAMER ? Cooking.ENABLE_STEAMER : meta == FRIDGE_TOP ? false : true;
    }

    @Override
    public int getMetaCount() {
        return 8;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        BlockIcons.registerBlockIcons(register);
    }
}
