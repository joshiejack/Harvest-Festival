package joshie.harvest.core.util.base;

import com.cricketcraft.ctmlib.ICTMBlock;
import com.cricketcraft.ctmlib.ISubmapManager;
import com.cricketcraft.ctmlib.RenderBlocksCTM;
import com.cricketcraft.ctmlib.SubmapManagerCTM;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * Convenience implementation of ICMBlock. This does everything you should need for basic CTM.
 * <p>
 * This class does <i>not</i> handle metadata-dependent textures.
 */

public abstract class CTMBlockHFBase extends BlockHFBase implements ICTMBlock<ISubmapManager> {

	@SideOnly(Side.CLIENT)
	private SubmapManagerCTM managerCTM;
	
	public RenderBlocksCTM rendererCTM = new RenderBlocksCTM();

	public abstract ISubmapManager getSubMap();
	
	private final String modid;
	private final String texturePath;
	
	private final int renderId;

	public CTMBlockHFBase(String modid, String texturePath, int renderId) {
		super(Material.ground);
		this.modid = modid;
		this.texturePath = texturePath;
		this.renderId = renderId;
	}

	@Override
	@SideOnly(Side.CLIENT)
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

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return HFBlocks.renderIDCTM;
	}

	@Override
	public ISubmapManager getManager(IBlockAccess world, int x, int y, int z, int meta) {
		return managerCTM;
	}

	@Override
	public ISubmapManager getManager(int meta) {
		return managerCTM;
	}
	
}
