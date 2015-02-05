package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.helpers.ShippingHelper.addForShipping;
import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.blocks.items.ItemBlockGeneral;
import joshie.harvestmoon.blocks.tiles.TileCooking;
import joshie.harvestmoon.blocks.tiles.TileFridge;
import joshie.harvestmoon.blocks.tiles.TileFryingPan;
import joshie.harvestmoon.blocks.tiles.TileKitchen;
import joshie.harvestmoon.blocks.tiles.TileMixer;
import joshie.harvestmoon.blocks.tiles.TileOven;
import joshie.harvestmoon.blocks.tiles.TilePot;
import joshie.harvestmoon.blocks.tiles.TileSteamer;
import joshie.harvestmoon.handlers.GuiHandler;
import joshie.harvestmoon.lib.RenderIds;
import joshie.harvestmoon.util.IShippable;
import joshie.lib.helpers.ItemHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGeneral extends BlockHMBaseMeta {
    public static final int SHIPPING = 0;
    public static final int FRIDGE = 1;
    public static final int KITCHEN = 2;
    public static final int POT = 3;
    public static final int FRYING_PAN = 4;
    public static final int MIXER = 5;
    public static final int OVEN = 6;
    public static final int STEAMER = 7;

    public BlockGeneral() {
        super(Material.wood);
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
        else if (meta == SHIPPING && player.getCurrentEquippedItem() != null) {
            ItemStack held = player.getCurrentEquippedItem();
            if (held.getItem() instanceof IShippable) {
                int sell = ((IShippable) held.getItem()).getSellValue(held);
                if (sell > 0) {
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }

                    return addForShipping(player, held);
                } else return false;
            } else return false;
        } else if (meta == FRIDGE) {
            player.openGui(HarvestMoon.instance, GuiHandler.COOKING, world, x, y, z);
            return true;
        } else {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof TileCooking) {
                TileCooking cooking = (TileCooking) tile;
                ItemStack held = player.getCurrentEquippedItem();
                if (!cooking.canAddItems()) {
                    if (!player.inventory.addItemStackToInventory(cooking.getStored())) {
                        if (!world.isRemote) {
                            ItemHelper.spawnItem(world, x, y + 1, z, cooking.getStored());
                        }
                    }

                    cooking.clear();
                } else if (held != null && !(held.getItem() instanceof ItemBlockGeneral)) {
                    if (cooking.addIngredient(held) || cooking.addSeasoning(held)) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }
                }
            }

            return false;
        }
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return meta != SHIPPING;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        return;
    }

    @Override
    public int getMetaCount() {
        return 8;
    }
}
