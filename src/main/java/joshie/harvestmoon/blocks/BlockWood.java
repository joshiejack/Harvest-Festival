package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.helpers.ShippingHelper.addForShipping;
import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.animals.AnimalType;
import joshie.harvestmoon.api.IShippable;
import joshie.harvestmoon.blocks.tiles.TileRuralChest;
import joshie.harvestmoon.cooking.FoodRegistry;
import joshie.harvestmoon.helpers.AnimalHelper;
import joshie.harvestmoon.helpers.generic.DirectionHelper;
import joshie.harvestmoon.init.cooking.HMIngredients;
import joshie.harvestmoon.lib.RenderIds;
import joshie.harvestmoon.util.generic.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockWood extends BlockHMBaseMeta {
    public static final int SHIPPING = 0;
    public static final int RURAL_CHEST = 1;
    public static final int NEST = 2;
    public static final int TROUGH = 3;
    public static final int TROUGH_2 = 4;

    public static final int OLD_RURAL_CHEST = 9;

    public BlockWood() {
        super(Material.wood);
        setHardness(1.5F);
    }

    @Override
    public String getToolType(int meta) {
        return "axe";
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
                long sell = ((IShippable) held.getItem()).getSellValue(held);
                if (sell > 0) {
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }

                    return addForShipping(player, held);
                } else return false;
            } else return false;
        } else if (meta == RURAL_CHEST) {
            player.openGui(HarvestMoon.instance, -1, world, x, y, z);
            return true;
        } else if (meta == NEST) {
            ItemStack held = player.getCurrentEquippedItem();
            if (held != null && FoodRegistry.getIngredients(held).contains(HMIngredients.egg)) {
                if (AnimalHelper.addEgg(world, x, y, z)) {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    return true;
                }
            }

            return false;
        } else if (meta == TROUGH) {
            ItemStack held = player.getCurrentEquippedItem();
            if (held != null && AnimalType.COW.canEat(held)) {
                if (AnimalHelper.addFodder(world, x, y, z)) {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    return true;
                }
            }

            return false;
        } else return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        ForgeDirection dir = DirectionHelper.getFacingFromEntity(entity);
        int meta = stack.getItemDamage();
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IFaceable) {
            ((IFaceable) tile).setFacing(dir);
        }

        if (meta == TROUGH || meta == TROUGH_2) {
            if (dir == ForgeDirection.WEST || dir == ForgeDirection.EAST) {
                world.setBlockMetadataWithNotify(x, y, z, TROUGH_2, 2);
            } else if (dir == ForgeDirection.NORTH || dir == ForgeDirection.SOUTH) {
                world.setBlockMetadataWithNotify(x, y, z, TROUGH, 2);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == NEST) {
            AnimalHelper.addNest(world, x, y, z);
        } else if (meta == TROUGH) {
            AnimalHelper.addTrough(world, x, y, z);
        }
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == NEST) {
            AnimalHelper.removeNest(world, x, y, z);
        } else if (meta == TROUGH) {
            AnimalHelper.removeTrough(world, x, y, z);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (meta == NEST) {
            AnimalHelper.removeNest(world, x, y, z);
        } else if (meta == TROUGH) {
            AnimalHelper.removeTrough(world, x, y, z);
        }
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return meta != SHIPPING;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        switch (meta) {
            case OLD_RURAL_CHEST:
                return new TileRuralChest();
            case RURAL_CHEST:
                return new TileRuralChest();
            default:
                return null;
        }
    }

    @Override
    public boolean isActive(int meta) {
        return meta != TROUGH_2;
    }

    @Override
    public int getMetaCount() {
        return 5;
    }
}
