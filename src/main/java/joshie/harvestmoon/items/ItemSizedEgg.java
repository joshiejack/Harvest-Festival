package joshie.harvestmoon.items;

import static joshie.harvestmoon.helpers.SizeableHelper.getQuality;
import static joshie.harvestmoon.helpers.SizeableHelper.getSize;
import static joshie.harvestmoon.helpers.SizeableHelper.getType;

import java.util.List;

import joshie.harvestmoon.HarvestTab;
import joshie.harvestmoon.helpers.SizeableHelper;
import joshie.harvestmoon.lib.HMModInfo;
import joshie.harvestmoon.lib.SizeableMeta;
import joshie.harvestmoon.lib.SizeableMeta.Size;
import joshie.harvestmoon.util.IRateable;
import joshie.harvestmoon.util.IShippable;
import joshie.harvestmoon.util.Translate;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSizedEgg extends ItemEgg implements IShippable, IRateable {
    private IIcon[] icons;

    public ItemSizedEgg() {
        setCreativeTab(HarvestTab.hm);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        return stack; //Do Sweet! nothing No cheaty eggs!
    }

    @Override
    public long getSellValue(ItemStack stack) {
        SizeableMeta meta = SizeableMeta.EGG;
        double quality = 1 + (getQuality(stack.getItemDamage()) * SELL_QUALITY_MODIFIER);
        return (long) quality * meta.getSellValue(getSize(stack.getItemDamage()));
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Size sizeof = getSize(stack.getItemDamage());
        String text = Translate.translate("sizeable.format");
        String size = Translate.translate("sizeable." + sizeof.name().toLowerCase());
        String name = Translate.translate("sizeable.egg");
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

    public String getName(ItemStack stack) {
        SizeableMeta meta = SizeableMeta.values()[getType(stack.getItemDamage())];
        return meta.name().toLowerCase();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[3];
        icons[0] = register.registerIcon(HMModInfo.MODPATH + ":egg_small");
        icons[1] = register.registerIcon(HMModInfo.MODPATH + ":egg_medium");
        icons[2] = register.registerIcon(HMModInfo.MODPATH + ":egg_large");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < 100; j += 100) {
            list.add(new ItemStack(item, 1, (j * 100)));
            list.add(new ItemStack(item, 1, 10000 + (j * 100)));
            list.add(new ItemStack(item, 1, 20000 + (j * 100)));
        }
    }
}
