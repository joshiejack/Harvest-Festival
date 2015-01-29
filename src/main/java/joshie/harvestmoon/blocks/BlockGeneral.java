package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.helpers.ShippingHelper.addForShipping;
import joshie.harvestmoon.HarvestMoon;
import joshie.harvestmoon.handlers.GuiHandler;
import joshie.harvestmoon.lib.RenderIds;
import joshie.harvestmoon.util.IShippable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
            return true;
        } else if (meta == FRIDGE) {
            player.openGui(HarvestMoon.instance, GuiHandler.COOKING, world, x, y, z);
            return true;
        } else return false;
    }

    @Override
    public boolean hasTileEntity(int meta) {
        return meta == FRIDGE;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        return meta == FRIDGE ? new TileFridge() : null;
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
