package joshie.harvest.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cricketcraft.ctmlib.CTMBlock;
import com.cricketcraft.ctmlib.ICTMBlock;
import com.cricketcraft.ctmlib.ISubmapManager;
import com.cricketcraft.ctmlib.SubmapManagerCTM;
import com.cricketcraft.ctmlib.TextureSubmap;

import joshie.harvest.HarvestFestival;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.render.HFSubmapManagerCTM;
import joshie.harvest.core.util.base.BlockHFBase;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.core.util.base.CTMBlockHFBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirt extends CTMBlockHFBase {
	
	private SubmapManagerCTM managerCTM;
	private HFSubmapManagerCTM dirtManagerCTM;

	public BlockDirt(String modid, String texturePath) {
		super(modid, texturePath, HFBlocks.renderIDCTM);
		this.setBlockUnbreakable();
		this.setResistance(6000000.0F);
		this.setHardness(50.0F);
		this.setCreativeTab(HFTab.tabMining);
	}

	@Override
	public ISubmapManager getSubMap() {
		return dirtManagerCTM;
	}

	
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        return !EntityHelper.isFakePlayer(player) ? 0.025F : super.getPlayerRelativeBlockHardness(player, world, x, y, z);
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
    
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
    {
    	return false;
    }

	/*
     Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors
    private static int MAXIMUM_FLOORS = 23;

    public static enum FloorType {
        ALL_FLOORS, MULTIPLE_OF_5, MULTIPLE_OF_10, MULTIPLE_OF_3, MULTIPLE_OF_2, ENDS_IN_8, ENDS_IN_9, LAST_FLOOR, MYSTRIL_FLOOR, GOLD_FLOOR, MYTHIC_FLOOR, CURSED_FLOOR, NON_MULTIPLE_OF_5, BELOW_15, GODDESS_FLOOR, BERRY_FLOOR;
    }
    */

}