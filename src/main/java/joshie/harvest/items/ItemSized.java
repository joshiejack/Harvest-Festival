package joshie.harvest.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.core.ISizeable;
import joshie.harvest.api.core.ISizeable.Size;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.lib.SizeableMeta;
import joshie.harvest.core.util.Translate;
import joshie.harvest.core.util.base.ItemHFBaseMeta;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static joshie.harvest.core.helpers.SizeableHelper.getSize;

public class ItemSized extends ItemHFBaseMeta implements IShippable, ICreativeSorted, ISizedProvider {
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
        return meta.getSellValue(getSize(stack.getItemDamage()));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getName(stack) + "_" + meta.getSize(stack).toString().toLowerCase();
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

    @Override
    public String getName(ItemStack stack) {
        return meta.name().toLowerCase();
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{HFTab.FARMING};
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        if (!meta.isVanilla()) {
            list.add(new ItemStack(item, 1, 0));
            list.add(new ItemStack(item, 1, 1));
            list.add(new ItemStack(item, 1, 2));
        }
    }
}