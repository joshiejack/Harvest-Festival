package joshie.harvest.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockStone extends BlockHFBaseMeta {
  
    public BlockStone() {
        super(Material.rock, HFTab.tabMining);
    }
    
	//META STUFF
    private static int META_COUNT = 2;
    
    @Override
    public int getMetaCount() {
        return META_COUNT;
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
	
	//TECHNICAL
    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        switch (world.getBlockMetadata(x, y, z)) {
            case 0:
                return 55F;
            case 1:
                return 4F;
            default:
                return 4F;
        }
    }
    
    @Override
    public int getToolLevel(int meta) {
    	return 2;
    }
    
    @Override
    public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
        switch (world.getBlockMetadata(x, y, z)) {
            case 0:
                return 6002;
            case 1:
                return 14.0F;
            default:
                return 5;
        }
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
        	if (metadata == 0) {
        	}
        	if (metadata == 1) {
                ret.add(new ItemStack(HFBlocks.stone, 1, 1));
            }
        }
        return ret;
    }
    
    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
    	return false;
    }

	
    //MINE STUFF
    /*
    public static final int CAVE_WALL = 0;
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int side) {
        if (world.rand.nextInt(3) == 0) {
            //MineHelper.caveIn(world, x, y, z);
        }
    }
    
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        return !EntityHelper.isFakePlayer(player) ? 0.025F : super.getPlayerRelativeBlockHardness(player, world, x, y, z);
    }
    */
}
