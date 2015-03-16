package joshie.harvestmoon.items;

import static joshie.harvestmoon.core.lib.HMModInfo.CROPPATH;

import java.util.List;

import joshie.harvestmoon.api.core.ICreativeSorted;
import joshie.harvestmoon.api.core.IRateable;
import joshie.harvestmoon.api.core.IShippable;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropProvider;
import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.lib.CreativeSort;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrop extends ItemHMMeta implements IShippable, IRateable, ICropProvider, ICreativeSorted {
    private ICrop crop;

    public ItemCrop(ICrop crop) {
        setCreativeTab(HMTab.tabGeneral);
        setTextureFolder(CROPPATH);
        this.crop = crop;
    }

    @Override
    public int getMetaCount() {
        return 1;
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.CROPS;
    }

    @Override
    public long getSellValue(ItemStack stack) {
        if (crop.isStatic()) return 0;
        double quality = (1 + CropHelper.getCropQualityFromDamage(stack.getItemDamage())) * General.SELL_QUALITY_MODIFIER;
        return (long) (quality * crop.getSellValue());
    }

    @Override
    public ICrop getCrop(ItemStack stack) {
        return crop;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return crop.getLocalizedName(true);
    }

    @Override
    public int getRating(ItemStack stack) {
        if (crop.isStatic()) return -1;
        else return CropHelper.getCropQualityFromDamage(stack.getItemDamage()) / 10;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        return icons[0];
    }

    @Override
    public String getName(ItemStack stack) {
        return crop.getUnlocalizedName();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < 100; j += Crops.CROP_QUALITY_LOOP) {
            ItemStack stack = new ItemStack(item, 1, (j * 100));
            if ((crop.isStatic() && j == 0) || !crop.isStatic()) {
                list.add(stack);
            }
        }
    }
}
