package joshie.harvestmoon.core.util.base;

import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.util.generic.IHasMetaBlock;
import joshie.harvestmoon.core.util.generic.IHasMetaItem;
import joshie.harvestmoon.core.util.generic.Text;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class ItemBlockBase extends ItemBlock implements IHasMetaItem {
    public ItemBlockBase(Block block) {
        super(block);
        setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(int meta) {
        return meta;
    }
    
    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[] { HMTab.tabFarming, HMTab.tabCooking, HMTab.tabMining, HMTab.tabTown };
    }
    
    @Override
    public int getMetaCount() {
        return ((IHasMetaBlock) Block.getBlockFromItem(this)).getMetaCount();
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String unlocalized = field_150939_a.getUnlocalizedName().replace("tile.", "").replace("_", ".");
        String name = getName(stack).replaceAll("(.)([A-Z])", "$1$2").toLowerCase();
        return Text.localize(unlocalized + "." + name.replace("_", "."));
    }
}
