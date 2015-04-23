package joshie.harvestmoon.blocks;

import java.util.List;

import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.init.HMBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirt extends BlockHMBaseMeta {
    /** Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors **/
    private static int MAXIMUM_FLOORS = 23;

    public static enum FloorType {
        ALL_FLOORS, MULTIPLE_OF_5, MULTIPLE_OF_10, MULTIPLE_OF_3, MULTIPLE_OF_2, ENDS_IN_8, ENDS_IN_9, LAST_FLOOR, MYSTRIL_FLOOR, GOLD_FLOOR, MYTHIC_FLOOR, CURSED_FLOOR, NON_MULTIPLE_OF_5, BELOW_15, GODDESS_FLOOR, BERRY_FLOOR;
    }

    public BlockDirt() {
        super(Material.ground, HMTab.tabMining);
    }

    @Override
    public String getToolType(int meta) {
        return "shovel";
    }

    //Lazy ass connected textures
    private IIcon[] top;
    private IIcon[] bottom;
    private IIcon[] left;
    private IIcon[] right;
    private IIcon[] top_left;
    private IIcon[] top_right;
    private IIcon[] bottom_left;
    private IIcon[] bottom_right;

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        if (side != 1) return HMBlocks.stone.getIcon(0, 0);

        int meta = world.getBlockMetadata(x, y, z);
        boolean xPlus = world.getBlock(x + 1, y, z) == this;
        boolean xMinus = world.getBlock(x - 1, y, z) == this;
        boolean zPlus = world.getBlock(x, y, z + 1) == this;
        boolean zMinus = world.getBlock(x, y, z - 1) == this;

        if (xPlus && xMinus && zPlus && zMinus) {
            return icons[meta];
        }

        if (xPlus && xMinus && zPlus) {
            return top[meta];
        }

        if (xPlus && xMinus && zMinus) {
            return bottom[meta];
        }

        if (xPlus && zPlus && zMinus) {
            return left[meta];
        }

        if (xMinus && zPlus && zMinus) {
            return right[meta];
        }

        if (xPlus && zPlus) {
            return top_left[meta];
        }

        if (xMinus && zPlus) {
            return top_right[meta];
        }

        if (xPlus && zMinus) {
            return bottom_left[meta];
        }

        if (xMinus && zMinus) {
            return bottom_right[meta];
        }

        return icons[meta];
    }

    @Override
    public int getMetaCount() {
        return 16;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String name = prefix != null ? prefix : "";
        icons = new IIcon[16];
        top = new IIcon[16];
        bottom = new IIcon[16];
        left = new IIcon[16];
        right = new IIcon[16];
        top_left = new IIcon[16];
        top_right = new IIcon[16];
        bottom_left = new IIcon[16];
        bottom_right = new IIcon[16];

        for (int i = 0; i < 16; i++) {
            icons[i] = iconRegister.registerIcon(mod + ":mine/middle_" + (i + 1));
            top[i] = iconRegister.registerIcon(mod + ":mine/top_" + (i + 1));
            bottom[i] = iconRegister.registerIcon(mod + ":mine/bottom_" + (i + 1));
            left[i] = iconRegister.registerIcon(mod + ":mine/left_" + (i + 1));
            right[i] = iconRegister.registerIcon(mod + ":mine/right_" + (i + 1));
            top_left[i] = iconRegister.registerIcon(mod + ":mine/top_left_" + (i + 1));
            top_right[i] = iconRegister.registerIcon(mod + ":mine/top_right_" + (i + 1));
            bottom_left[i] = iconRegister.registerIcon(mod + ":mine/bottom_left_" + (i + 1));
            bottom_right[i] = iconRegister.registerIcon(mod + ":mine/bottom_right_" + (i + 1));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (General.DEBUG_MODE) {
            for (int i = 0; i < 16; i++) {
                if (isValidTab(tab, i)) {
                    list.add(new ItemStack(item, 1, i));
                }
            }
        }
    }
}
