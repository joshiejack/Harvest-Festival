package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.blocks.BlockDirt.FloorType.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import joshie.harvestmoon.config.General;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirt extends BlockHMBaseMeta {
    /** Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors **/
    public static int MAXIMUM_FLOORS = 23;
    
    public static enum FloorType {
        ALL_FLOORS,
        MULTIPLE_OF_5,
        MULTIPLE_OF_10,
        MULTIPLE_OF_3,
        MULTIPLE_OF_2,
        ENDS_IN_8,
        ENDS_IN_9,
        LAST_FLOOR,
        MYSTRIL_FLOOR,
        GOLD_FLOOR,
        MYTHIC_FLOOR,
        CURSED_FLOOR,
        NON_MULTIPLE_OF_5,
        BELOW_15,
        GODDESS_FLOOR,
        BERRY_FLOOR;
    }
    
    private static final Random rand = new Random();

    public static ArrayList<Integer> getMeta(int level) {
        ArrayList<Integer> metas = new ArrayList(16);
        metas.add(ALL_FLOORS.ordinal());
        if (level % 5 == 0) metas.add(MULTIPLE_OF_5.ordinal());
        if (level % 10 == 0) metas.add(MULTIPLE_OF_10.ordinal());
        if (level % 3 == 0) metas.add(MULTIPLE_OF_3.ordinal());
        if (level % 2 == 0) metas.add(MULTIPLE_OF_2.ordinal());
        if (level % 10 == 8) metas.add(ENDS_IN_8.ordinal());
        if (level % 10 == 9) metas.add(ENDS_IN_9.ordinal());
        if (level == MAXIMUM_FLOORS) metas.add(LAST_FLOOR.ordinal());
        if (level >= 5) metas.add(MYSTRIL_FLOOR.ordinal());
        if (level >= 3) metas.add(GOLD_FLOOR.ordinal());
        if (level == 13) metas.add(MYTHIC_FLOOR.ordinal());
        if (rand.nextInt(20) == level) metas.add(CURSED_FLOOR.ordinal());
        if (level % 5 != 0) metas.add(NON_MULTIPLE_OF_5.ordinal());
        if (level > 15) metas.add(BELOW_15.ordinal());
        if (level == 16 || level == 14) metas.add(GODDESS_FLOOR.ordinal());
        if (level == 15) metas.add(BERRY_FLOOR.ordinal());
        return metas;
    }

    public BlockDirt() {
        super(Material.ground);
        setHardness(1F);
    }
    
    @Override
    public String getToolType(int meta) {
        return "shovel";
    }

    @Override
    public int getMetaCount() {
        return 16;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String name = prefix != null ? prefix : "";
        icons = new IIcon[1];
        icons[0] = iconRegister.registerIcon(mod + ":" + name + getName(0));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (General.DEBUG_MODE) {
            for (int i = 0; i < 16; i++) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
}
