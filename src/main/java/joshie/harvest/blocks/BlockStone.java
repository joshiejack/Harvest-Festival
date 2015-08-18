package joshie.harvest.blocks;

import java.util.List;
import java.util.Random;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStone extends BlockHFBaseMeta {
    public static final int CAVE_WALL = 0;
    private static int META_COUNT = 6;
    
    public BlockStone() {
        super(Material.rock, HFTab.tabMining);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setHardness(1000.0F);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        return !EntityHelper.isFakePlayer(player) ? 0.025F : super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }

    @Override
    public int getToolLevel(int meta) {
        return 1;
    }

    @Override
    public int getMetaCount() {
        return META_COUNT;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int side) {
        if (world.rand.nextInt(3) == 0) {
            //MineHelper.caveIn(world, x, y, z);
        }
    }
    
    @Override
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }
    
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
    	return false;
    }
    
    //Maybe something doable in the future
    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int p_149681_5_, EntityPlayer player) {
    	
    }


    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        if (General.DEBUG_MODE) {
            for (int i = 0; i < getMetaCount(); i++) {
                if (isValidTab(tab, i)) {
                    list.add(new ItemStack(item, 1, i));
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String name = prefix != null ? prefix : "";
        icons = new IIcon[getMetaCount()];

        icons[0] = iconRegister.registerIcon(mod + ":" + name + getName(0));
        for (int i = 1; i < icons.length; i++) {
            icons[i] = iconRegister.registerIcon(mod + ":" + name + getName(0) + i);
        }
    }

    public static int getRandomMeta(Random rand) {
        if (rand.nextInt(13) <= 10) {
            return 0;
        }

        return rand.nextInt(META_COUNT);
    }
}
