package joshie.harvest.core.util.base;

import net.minecraft.block.material.Material;

/**
 * Convenience implementation of ICMBlock. This does everything you should need for basic CTM.
 * <p>
 * This class does <i>not</i> handle metadata-dependent textures.
 */

public abstract class CTMBlockHFBase<E extends Enum> extends BlockHFBaseMeta {

	/*@SideOnly(Side.CLIENT)
	private SubmapManagerCTM managerCTM;

	public RenderBlocksCTM rendererCTM = new RenderBlocksCTM();

	public abstract ISubmapManager getSubMap();*/

	public CTMBlockHFBase(Class<E> clazz) {
		super(Material.GROUND, clazz);
		/*this.modid = modid;
		this.texturePath = texturePath;
		this.renderId = renderId;*/
	}

	/*@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return HFBlocks.renderIDCTM;
	}*/

	/*@Override
	public ISubmapManager getManager(IBlockAccess world, int x, int y, int z, int meta) {
		return managerCTM;
	}*/

	/*@Override
	public ISubmapManager getManager(int meta) {
		return managerCTM;
	}*/
	
}
