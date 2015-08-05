package joshie.harvest.blocks;

import java.util.ArrayList;
import java.util.List;

import com.cricketcraft.ctmlib.ICTMBlock;
import com.cricketcraft.ctmlib.ISubmapManager;
import com.cricketcraft.ctmlib.SubmapManagerCTM;
import com.cricketcraft.ctmlib.TextureSubmap;

import joshie.harvest.api.core.HFSubmapManagerCTM;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.util.base.BlockHFBase;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirt extends BlockHFBase implements ICTMBlock<ISubmapManager> {
	
	@SideOnly(Side.CLIENT)
	private SubmapManagerCTM manager;
	 
	private final int renderId;
	
    /** Normal height = 12 floors, y91 = On a hill = 17 floors, On an extreme hills = y120 = 23 floors **/
    private static int MAXIMUM_FLOORS = 23;

    public static enum FloorType {
        ALL_FLOORS, MULTIPLE_OF_5, MULTIPLE_OF_10, MULTIPLE_OF_3, MULTIPLE_OF_2, ENDS_IN_8, ENDS_IN_9, LAST_FLOOR, MYSTRIL_FLOOR, GOLD_FLOOR, MYTHIC_FLOOR, CURSED_FLOOR, NON_MULTIPLE_OF_5, BELOW_15, GODDESS_FLOOR, BERRY_FLOOR;
    }

    public BlockDirt(int renderId) {
        super(Material.ground);
    	this.setHarvestLevel("shovel", 0);
		this.renderId = renderId;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return renderId;
	}


	@Override
	public ISubmapManager getManager(IBlockAccess world, int x, int y, int z, int meta) {
		return manager;
	}

	@Override
	public ISubmapManager getManager(int meta) {
		return manager;
	}

}
