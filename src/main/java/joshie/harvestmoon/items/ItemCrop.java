package joshie.harvestmoon.items;

import static joshie.harvestmoon.core.helpers.CropHelper.getCropQuality;
import static joshie.harvestmoon.core.helpers.CropHelper.isGiant;
import static joshie.harvestmoon.core.lib.HMModInfo.CROPPATH;

import java.util.List;

import joshie.harvestmoon.api.interfaces.ICropProvider;
import joshie.harvestmoon.api.interfaces.IRateable;
import joshie.harvestmoon.api.interfaces.IShippable;
import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.config.General;
import joshie.harvestmoon.crops.Crop;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrop extends ItemHMMeta implements IShippable, IRateable, ICropProvider {
    private Crop crop;

    public ItemCrop(Crop crop) {
        setCreativeTab(HMTab.tabGeneral);
        setTextureFolder(CROPPATH);
        this.crop = crop;
    }

    @Override
    public int getMetaCount() {
        return 1;
    }

    @Override
    public long getSellValue(ItemStack stack) {
        if (crop.isStatic()) return 0;
        boolean isGiant = isGiant(stack.getItemDamage());
        double quality = 1 + (getCropQuality(stack.getItemDamage()) * General.SELL_QUALITY_MODIFIER);
        long ret = (int) quality * crop.getSellValue();
        return isGiant ? ret * 10 : ret;
    }

    @Override
    public Crop getCrop(ItemStack stack) {
        return crop;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return crop.getCropName(true, isGiant(stack.getItemDamage()));
    }

    @Override
    public int getRating(ItemStack stack) {
        if (crop.isStatic()) return -1;
        else return getCropQuality(stack.getItemDamage()) / 10;
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
        for (int j = 0; j < 100; j += 100) {
            ItemStack stack = new ItemStack(item, 1, (j * 100));
            if ((crop.isStatic() && j == 0) || !crop.isStatic()) {
                list.add(stack);
            }
        }
    }
}
