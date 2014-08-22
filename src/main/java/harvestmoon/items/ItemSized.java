package harvestmoon.items;

import static harvestmoon.helpers.SizeableHelper.getQuality;
import static harvestmoon.helpers.SizeableHelper.getSize;
import static harvestmoon.helpers.SizeableHelper.getType;
import harvestmoon.HarvestTab;
import harvestmoon.helpers.SizeableHelper;
import harvestmoon.lib.SizeableMeta;
import harvestmoon.lib.SizeableMeta.Size;
import harvestmoon.util.IRateable;
import harvestmoon.util.IShippable;
import harvestmoon.util.Text;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSized extends ItemBaseMeta implements IShippable, IRateable {
    public ItemSized() {
        setCreativeTab(HarvestTab.hm);
        setMaxMetaDamage(SizeableMeta.values().length);
    }

    @Override
    public int getSellValue(ItemStack stack) {
        SizeableMeta meta = SizeableMeta.values()[getType(stack.getItemDamage())];
        double quality = 1 + (getQuality(stack.getItemDamage()) * SELL_QUALITY_MODIFIER);
        return (int) quality * meta.getSellValue(getSize(stack.getItemDamage()));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Size sizeof = getSize(stack.getItemDamage());
        int sizeable = getType(stack.getItemDamage());
        SizeableMeta sized = SizeableMeta.values()[sizeable < SizeableMeta.values().length ? sizeable : 0];
        String text = Text.translate("sizeable.format");
        String size = Text.translate("sizeable." + sizeof.name().toLowerCase());
        String name = Text.translate("sizeable." + sized.name().toLowerCase());
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
    public String getName(int dmg) {
        SizeableMeta meta = SizeableMeta.values()[getType(dmg)];
        return meta.name().toLowerCase();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[meta * 3];
        for (int i = 0; i < icons.length; i += 3) {
            icons[i] = register.registerIcon(path + getName(i / 3) + "_small");
            icons[i + 1] = register.registerIcon(path + getName(i / 3) + "_medium");
            icons[i + 2] = register.registerIcon(path + getName(i / 3) + "_large");
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < meta; i++) {
            for (int j = 0; j < 100; j += 100) {
                list.add(new ItemStack(item, 1, (j * 100) + i));
                list.add(new ItemStack(item, 1, 10000 + (j * 100) + i));
                list.add(new ItemStack(item, 1, 20000 + (j * 100) + i));
            }
        }
    }
}
