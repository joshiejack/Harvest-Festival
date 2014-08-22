package harvestmoon.blocks;

import static harvestmoon.helpers.ShippingHelper.addForShipping;
import harvestmoon.HarvestMoon;
import harvestmoon.HarvestTab;
import harvestmoon.blocks.items.ItemBlockGeneral;
import harvestmoon.handlers.GuiHandler;
import harvestmoon.util.IShippable;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGeneral extends BlockBase {
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
        setCreativeTab(HarvestTab.hm);
    }

    @Override
    public BlockGeneral register() {
        GameRegistry.registerBlock((Block) this, ItemBlockGeneral.class, super.getUnlocalizedName());
        return this;
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
    public int damageDropped(int i) {
        return i;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        return;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, SHIPPING));
        list.add(new ItemStack(item, 1, FRIDGE));
        list.add(new ItemStack(item, 1, KITCHEN));
        list.add(new ItemStack(item, 1, POT));
        list.add(new ItemStack(item, 1, FRYING_PAN));
        list.add(new ItemStack(item, 1, MIXER));
        list.add(new ItemStack(item, 1, OVEN));
        list.add(new ItemStack(item, 1, STEAMER));
    }
}
