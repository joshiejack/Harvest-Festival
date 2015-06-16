package joshie.harvest.asm.overrides;

import static joshie.harvest.core.helpers.SizeableHelper.getSize;

import java.util.List;

import joshie.harvest.api.core.ISizeable;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.core.util.Translate;
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
        return SizeableMeta.EGG.getSellValue(getSize(stack.getItemDamage()));
    }
    
    public static int getSortValue() {
        return CreativeSort.SIZEABLE;
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
    
    public static ISizeable getSizeable(ItemStack stack) {
        return SizeableMeta.EGG;
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
        icons[0] = register.registerIcon(HFModInfo.MODPATH + ":egg_small");
        icons[1] = register.registerIcon(HFModInfo.MODPATH + ":egg_medium");
        icons[2] = register.registerIcon(HFModInfo.MODPATH + ":egg_large");
    }
    
    @SideOnly(Side.CLIENT)
    public static void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }
}
