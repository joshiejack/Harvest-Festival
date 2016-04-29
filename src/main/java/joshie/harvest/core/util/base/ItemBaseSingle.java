package joshie.harvest.core.util.base;

import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
        GameRegistry.register(this, new ResourceLocation(HFModInfo.MODID, name.replace(".", "_")));
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
