package joshie.harvest.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.render.HFSubmapManagerCTM;
import joshie.harvest.core.util.base.CTMBlockHFBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockDirt extends CTMBlockHFBase {
	public BlockDirt(String modid, String texturePath) {
		super(modid, texturePath, HFBlocks.renderIDCTM);
		setCreativeTab(HFTab.tabMining);
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
	
	//CTM
	private SubmapManagerCTM managerCTM;
	private HFSubmapManagerCTM dirtManagerCTM;

	@Override
	public ISubmapManager getSubMap() {
		return dirtManagerCTM;
	}

	//TECHNICAL/
    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        switch (world.getBlockMetadata(x, y, z)) {
            case 0:
                return 50F;
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
                return 6000;
            case 1:
                return 12.0F;
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
                ret.add(new ItemStack(HFBlocks.DIRT, 1, 1));
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

    //Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors
    private static int MAXIMUM_FLOORS = 23;

    public static enum FloorType {
        ALL_FLOORS, MULTIPLE_OF_5, MULTIPLE_OF_10, MULTIPLE_OF_3, MULTIPLE_OF_2, ENDS_IN_8, ENDS_IN_9, LAST_FLOOR, MYSTRIL_FLOOR, GOLD_FLOOR, MYTHIC_FLOOR, CURSED_FLOOR, NON_MULTIPLE_OF_5, BELOW_15, GODDESS_FLOOR, BERRY_FLOOR;
    }
}