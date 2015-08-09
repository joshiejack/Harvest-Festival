package joshie.harvest.blocks;

import java.util.ArrayList;
import java.util.List;

import com.cricketcraft.ctmlib.CTMBlock;
import com.cricketcraft.ctmlib.ICTMBlock;
import com.cricketcraft.ctmlib.ISubmapManager;
import com.cricketcraft.ctmlib.SubmapManagerCTM;
import com.cricketcraft.ctmlib.TextureSubmap;

import joshie.harvest.HarvestFestival;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockHFBase;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import joshie.harvest.core.util.base.CTMBlockHFBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirt extends CTMBlockHFBase {
	
	private SubmapManagerCTM managerCTM;

	public BlockDirt(String modid, String texturePath) {
		super(modid, texturePath, HFBlocks.renderIDCTM);
		this.setHarvestLevel("shovel", 0);
	}

	@Override
	public ISubmapManager getSubMap() {
		return managerCTM;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister icon) {
		managerCTM = new SubmapManagerCTM("dirt");
		managerCTM.registerIcons(HFModInfo.MODPATH, this, icon);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return managerCTM.getIcon(world, x, y, z, side);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return managerCTM.getIcon(side, meta);
	}


	/*
     Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors
    private static int MAXIMUM_FLOORS = 23;

    public static enum FloorType {
        ALL_FLOORS, MULTIPLE_OF_5, MULTIPLE_OF_10, MULTIPLE_OF_3, MULTIPLE_OF_2, ENDS_IN_8, ENDS_IN_9, LAST_FLOOR, MYSTRIL_FLOOR, GOLD_FLOOR, MYTHIC_FLOOR, CURSED_FLOOR, NON_MULTIPLE_OF_5, BELOW_15, GODDESS_FLOOR, BERRY_FLOOR;
    }
    */

}