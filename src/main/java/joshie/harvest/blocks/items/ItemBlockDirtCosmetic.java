package joshie.harvest.blocks.items;

import java.util.List;

import joshie.harvest.core.config.General;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.base.ItemBlockBase;
import joshie.harvest.blocks.items.ItemBlockDirt;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockDirtCosmetic extends ItemBlockDirt {
    public ItemBlockDirtCosmetic(Block block) {
        super(block);
    }
    
    @Override
    public String getName(ItemStack stack) {
        return "mine_floor_cosmetic";
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
			list.add(Translate.translate("tooltip.dirt"));
	}
}
