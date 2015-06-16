package joshie.harvest.items;

import static joshie.harvest.core.helpers.SizeableHelper.getSize;

import java.util.List;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.core.ISizeable;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.config.General;
import joshie.harvest.core.helpers.SizeableHelper;
import joshie.harvest.core.lib.CreativeSort;
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

public class ItemSized extends ItemHFMeta implements IShippable, ICreativeSorted, ISizedProvider {
    private final SizeableMeta meta;

    public ItemSized(SizeableMeta meta) {
        this.meta = meta;
    }

    @Override
    public ISizeable getSizeable(ItemStack stack) {
        return meta;
    }

    @Override
    public int getMetaCount() {
        return 4; //Only enable the sizeables for 0.5
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.SIZEABLE + stack.getItemDamage() + (meta.ordinal() * 3);
    }

    @Override
    public long getSellValue(ItemStack stack) {
        return (long) General.SELL_QUALITY_MODIFIER * SizeableMeta.EGG.getSellValue(getSize(stack.getItemDamage()));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Size sizeof = getSize(stack.getItemDamage());
        String text = Translate.translate("sizeable.format");
        String size = Translate.translate("sizeable." + sizeof.name().toLowerCase());
        String name = Translate.translate("sizeable." + meta.name().toLowerCase());
        text = StringUtils.replace(text, "%S", size);
        text = StringUtils.replace(text, "%P", name);
        return text;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {
        int size = SizeableHelper.getSize(damage).ordinal();
        if (size < icons.length && icons[size] != null) {
            return icons[size];
        } else return icons[0];
    }

    @Override
    public String getName(ItemStack stack) {
        return meta.name().toLowerCase();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        String path = this.path != null ? this.path : mod + ":";
        icons = new IIcon[getMetaCount() * 3];
        icons[0] = register.registerIcon(path + getName(new ItemStack(this)) + "_small");
        icons[1] = register.registerIcon(path + getName(new ItemStack(this)) + "_medium");
        icons[2] = register.registerIcon(path + getName(new ItemStack(this)) + "_large");
    }
    
    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[] { HFTab.tabFarming };
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        if (!meta.isVanilla()) {
            list.add(new ItemStack(item, 1, 0));
            list.add(new ItemStack(item, 1, 1));
            list.add(new ItemStack(item, 1, 2));
        }
    }
}
