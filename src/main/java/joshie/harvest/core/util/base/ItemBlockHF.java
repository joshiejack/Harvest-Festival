package joshie.harvest.core.util.base;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.core.HFTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockHF extends ItemBlock implements  ICreativeSorted {
    private final BlockHFBaseEnum block;

    public ItemBlockHF(BlockHFBaseEnum block) {
        super(block);
        this.block = block;
        setHasSubtypes(true);
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{ HFTab.FARMING, HFTab.COOKING, HFTab.MINING, HFTab.TOWN, HFTab.GATHERING };
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return block.getItemStackDisplayName(stack);
    }

    @Override
    public BlockHFBaseEnum getBlock() {
        return block;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return block.getEnumFromMeta(stack.getItemDamage()).name().toLowerCase();
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return block.getSortValue(stack);
    }
}