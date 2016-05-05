package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemHFBase extends Item {

    public ItemHFBase() {
        this(HFTab.FARMING);
    }

    public ItemHFBase(CreativeTabs tab) {
        setCreativeTab(tab);
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
}