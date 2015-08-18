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
import joshie.harvest.core.util.Translate;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirtCosmetic extends BlockDirt {

	public BlockDirtCosmetic(String modid, String texturePath) {
		super(modid, texturePath);
	}
}