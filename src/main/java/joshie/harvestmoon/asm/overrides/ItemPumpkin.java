package joshie.harvestmoon.asm.overrides;

import java.util.List;

import joshie.harvestmoon.api.core.ICreativeSorted;
import joshie.harvestmoon.api.core.IRateable;
import joshie.harvestmoon.api.core.IShippable;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropProvider;
import joshie.harvestmoon.core.lib.CreativeSort;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPumpkin extends ItemBlock implements IRateable, IShippable, ICropProvider, ICreativeSorted {
    public ItemPumpkin(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.CROPS;
    }
    
    @Override
    public ICrop getCrop(ItemStack stack) {
        return ItemSeedFood.getCrop(stack);
    }
    
    @Override
    public long getSellValue(ItemStack stack) {
        return ItemSeedFood.getSellValue(stack);
    }

    @Override
    public int getRating(ItemStack stack) {
        return ItemSeedFood.getRating(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return ItemSeedFood.getItemStackDisplayName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemSeedFood.getSubItems(item, tab, list);
    }
}
