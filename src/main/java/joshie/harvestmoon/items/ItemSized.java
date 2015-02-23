package joshie.harvestmoon.items;

import static joshie.harvestmoon.helpers.SizeableHelper.getQuality;
import static joshie.harvestmoon.helpers.SizeableHelper.getSize;
import static joshie.harvestmoon.helpers.SizeableHelper.getType;

import java.util.List;

import joshie.harvestmoon.HMConfiguration;
import joshie.harvestmoon.api.IRateable;
import joshie.harvestmoon.api.IShippable;
import joshie.harvestmoon.helpers.SizeableHelper;
import joshie.harvestmoon.lib.SizeableMeta;
import joshie.harvestmoon.lib.SizeableMeta.Size;
import joshie.harvestmoon.util.Translate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSized extends ItemHMMeta implements IShippable, IRateable {
    @Override
    public int getMetaCount() {
        return 4; //Only enable the sizeables for 0.5
    }

    @Override
    public long getSellValue(ItemStack stack) {
        SizeableMeta meta = SizeableMeta.values()[getType(stack.getItemDamage())];
        double quality = 1 + (getQuality(stack.getItemDamage()) * SELL_QUALITY_MODIFIER);
        return (long) quality * meta.getSellValue(getSize(stack.getItemDamage()));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Size sizeof = getSize(stack.getItemDamage());
        int sizeable = getType(stack.getItemDamage());
        SizeableMeta sized = SizeableMeta.values()[sizeable < SizeableMeta.values().length ? sizeable : 0];
        String text = Translate.translate("sizeable.format");
        String size = Translate.translate("sizeable." + sizeof.name().toLowerCase());
        String name = Translate.translate("sizeable." + sized.name().toLowerCase());
        text = StringUtils.replace(text, "%S", size);
        text = StringUtils.replace(text, "%P", name);
        return text;
    }

    @Override
    public int getRating(ItemStack stack) {
        int quality = SizeableHelper.getQuality(stack.getItemDamage());
        return quality / 10;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        int meta = SizeableHelper.getSize(damage).ordinal() + (getType(damage) * 3);
        if (meta < icons.length && icons[meta] != null) {
            return icons[meta];
        } else return icons[0];
    }

    @Override
    public String getName(ItemStack stack) {
        SizeableMeta meta = SizeableMeta.values()[getType(stack.getItemDamage())];
        return meta.name().toLowerCase();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        String path = this.path != null ? this.path : mod + ":";
        icons = new IIcon[getMetaCount() * 3];
        for (int i = 0; i < icons.length; i += 3) {
            icons[i] = register.registerIcon(path + getName(new ItemStack(this, 1, i / 3)) + "_small");
            icons[i + 1] = register.registerIcon(path + getName(new ItemStack(this, 1, i / 3)) + "_medium");
            icons[i + 2] = register.registerIcon(path + getName(new ItemStack(this, 1, i / 3)) + "_large");
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < getMetaCount(); i++) {
            for (int j = 0; j < 100; j += 100) {
                SizeableMeta meta = SizeableHelper.getSizeableFromStack(new ItemStack(item, 1, (j * 100) + i));
                if(meta != SizeableMeta.EGG || !HMConfiguration.vanilla.EGG_OVERRIDE) {
                    list.add(new ItemStack(item, 1, (j * 100) + i));
                    list.add(new ItemStack(item, 1, 10000 + (j * 100) + i));
                    list.add(new ItemStack(item, 1, 20000 + (j * 100) + i));
                }
            }
        }
    }
}
