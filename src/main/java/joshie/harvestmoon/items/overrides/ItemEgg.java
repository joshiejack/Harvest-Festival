package joshie.harvestmoon.items.overrides;

import static joshie.harvestmoon.core.helpers.SizeableHelper.getQuality;
import static joshie.harvestmoon.core.helpers.SizeableHelper.getSize;

import java.util.List;

import joshie.harvestmoon.api.interfaces.IShippable;
import joshie.harvestmoon.core.helpers.SizeableHelper;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.core.lib.SizeableMeta;
import joshie.harvestmoon.core.lib.SizeableMeta.Size;
import joshie.harvestmoon.core.util.Translate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEgg {
    public static IIcon[] icons;
    
    public static long getSellValue(ItemStack stack) {
        double quality = 1 + (getQuality(stack.getItemDamage()) * IShippable.SELL_QUALITY_MODIFIER);
        return (long) quality * SizeableMeta.EGG.getSellValue(getSize(stack.getItemDamage()));
    }
    
    public static int getRating(ItemStack stack) {
        return SizeableHelper.getQuality(stack.getItemDamage()) / 10;
    }
    
    public static String getItemStackDisplayName(ItemStack stack) {
        Size sizeof = getSize(stack.getItemDamage());
        String text = Translate.translate("sizeable.format");
        String size = Translate.translate("sizeable." + sizeof.name().toLowerCase());
        String name = Translate.translate("sizeable.egg");
        text = StringUtils.replace(text, "%S", size);
        text = StringUtils.replace(text, "%P", name);
        return text;
    }
    
    @SideOnly(Side.CLIENT)
    public static IIcon getIconFromDamage(int damage) {
        int size = SizeableHelper.getSize(damage).ordinal();
        if (size < icons.length && icons[size] != null) {
            return icons[size];
        } else return icons[0];
    }
    
    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister register) {
        icons = new IIcon[3];
        icons[0] = register.registerIcon(HMModInfo.MODPATH + ":egg_small");
        icons[1] = register.registerIcon(HMModInfo.MODPATH + ":egg_medium");
        icons[2] = register.registerIcon(HMModInfo.MODPATH + ":egg_large");
    }
    
    @SideOnly(Side.CLIENT)
    public static void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < 100; j += 100) {
            list.add(new ItemStack(item, 1, (j * 100)));
            list.add(new ItemStack(item, 1, 10000 + (j * 100)));
            list.add(new ItemStack(item, 1, 20000 + (j * 100)));
        }
    }
}
