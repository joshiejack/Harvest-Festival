package joshie.harvestmoon.items;

import static joshie.harvestmoon.core.lib.HMModInfo.MODPATH;
import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.util.Translate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemBaseSingle extends Item {
    protected String path = MODPATH + ":";
    
    public ItemBaseSingle() {
        setCreativeTab(HMTab.tabGeneral);
    }
    
    public void setTextureFolder(String path) {
        this.path = path;
    }
    
    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        GameRegistry.registerItem(this, name.replace(".", "_"));
        return this;
    }
    
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return Translate.translate(super.getUnlocalizedName().replace("item.", ""));
    }
}
