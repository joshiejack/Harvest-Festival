package joshie.harvest.items;

import static joshie.harvest.core.lib.HFModInfo.MODPATH;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.Translate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemBaseSingle extends Item {
    protected String path = MODPATH + ":";
    
    public ItemBaseSingle() {
        setCreativeTab(HFTab.tabFarming);
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
