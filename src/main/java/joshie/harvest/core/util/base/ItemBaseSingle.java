package joshie.harvest.core.util.base;

import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemBaseSingle extends Item {
    protected String mod;
    protected String path;

    public ItemBaseSingle(String mod, CreativeTabs tab) {
        setCreativeTab(tab);
        setMaxStackSize(1);
        this.mod = mod;
    }

    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        RegistryHelper.registerItem(this, name);
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return mod + "." + super.getUnlocalizedName().replace("item.", "").replace("_", ".");
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Text.localize(getUnlocalizedName());
    }
}
