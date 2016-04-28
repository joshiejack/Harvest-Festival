package joshie.harvest.asm.overrides;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.lib.CreativeSort;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemMelon extends ItemFood implements IShippable, ICropProvider, ICreativeSorted {
    public ItemMelon(int hunger, float saturation, boolean isMeat) {
        super(hunger, saturation, isMeat);
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
    public ItemMelon setUnlocalizedName(String string) {
        return (ItemMelon) super.setUnlocalizedName(string);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return ItemSeedFood.getSellValue(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return ItemSeedFood.getItemStackDisplayName(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        ItemSeedFood.getSubItems(item, tab, list);
    }
}