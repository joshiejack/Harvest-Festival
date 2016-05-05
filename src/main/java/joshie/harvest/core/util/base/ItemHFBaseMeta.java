package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.IHasMetaItem;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public abstract class ItemHFBaseMeta extends Item implements IHasMetaItem {

    public ItemHFBaseMeta() {
        this(HFTab.FARMING);
    }

    public ItemHFBaseMeta(CreativeTabs tab) {
        setCreativeTab(tab);
        setHasSubtypes(true);
    }

    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        RegistryHelper.registerItem(this, name);
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return HFModInfo.MODID + "." + super.getUnlocalizedName().replace("item.", "");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "_" + getName(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = getName(stack).replaceAll("(.)([A-Z])", "$1$2").toLowerCase();
        return Text.localize(getUnlocalizedName() + "." + name.replace("_", "."));
    }

    public String getName(ItemStack stack) {
        return "name";
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    public boolean isValidTab(CreativeTabs tab, int meta) {
        return tab == getCreativeTab();
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{HFTab.FARMING, HFTab.COOKING, HFTab.MINING, HFTab.TOWN};
    }

    public boolean isActive(int damage) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < getMetaCount(); i++) {
            if (isActive(i) && isValidTab(tab, i)) {
                list.add(new ItemStack(item, 1, i));
            }
        }
    }
}