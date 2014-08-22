package harvestmoon.items;

import static harvestmoon.lib.ModInfo.MODPATH;
import harvestmoon.HarvestTab;
import harvestmoon.util.Text;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemBaseSingle extends Item {
    protected String path = MODPATH + ":";
    
    public ItemBaseSingle() {
        setCreativeTab(HarvestTab.hm);
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
        return Text.translate(super.getUnlocalizedName().replace("item.", ""));
    }
}
