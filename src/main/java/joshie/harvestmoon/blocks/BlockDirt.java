package joshie.harvestmoon.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirt extends BlockHMBaseMeta {
    /** Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors **/
    public static int MAXIMUM_FLOORS = 23;
    public static final int ALL_FLOORS = 0;
    public static final int MULTIPLE_OF_5 = 1;
    public static final int MULTIPLE_OF_10 = 2;
    public static final int MULTIPLE_OF_3 = 3;
    public static final int MULTIPLE_OF_2 = 4;
    public static final int ENDS_IN_8 = 5;
    public static final int ENDS_IN_9 = 6;
    public static final int LAST_FLOOR = 8;
    public static final int MYSTRIL_FLOOR = 7;
    public static final int GOLD_FLOOR = 9;
    public static final int MYTHIC_FLOOR = 10;
    public static final int CURSED_FLOOR = 11;
    public static final int NON_MULTIPLE_OF_5 = 12;
    public static final int BELOW_15 = 13;
    public static final int GODDESS_FLOOR = 14;
    public static final int BERRY_FLOOR = 15;
    private static final Random rand = new Random();

    public static ArrayList<Integer> getMeta(int level) {
        ArrayList<Integer> metas = new ArrayList();
        metas.add(ALL_FLOORS);
        if (level % 5 == 0) metas.add(MULTIPLE_OF_5);
        if (level % 10 == 0) metas.add(MULTIPLE_OF_10);
        if (level % 3 == 0) metas.add(MULTIPLE_OF_3);
        if (level % 2 == 0) metas.add(MULTIPLE_OF_2);
        if (level % 10 == 8) metas.add(ENDS_IN_8);
        if (level % 10 == 9) metas.add(ENDS_IN_9);
        if (level == MAXIMUM_FLOORS) metas.add(LAST_FLOOR);
        if (level >= 5) metas.add(MYSTRIL_FLOOR);
        if (level >= 3) metas.add(GOLD_FLOOR);
        if (level == 13) metas.add(MYTHIC_FLOOR);
        if (rand.nextInt(20) == level) metas.add(CURSED_FLOOR);
        if (level % 5 != 0) metas.add(NON_MULTIPLE_OF_5);
        if (level > 15) metas.add(BELOW_15);
        if (level == 16 || level == 14) metas.add(GODDESS_FLOOR);
        if (level == 15) metas.add(BERRY_FLOOR);
        return metas;
    }

    public BlockDirt() {
        super(Material.ground);
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
}
