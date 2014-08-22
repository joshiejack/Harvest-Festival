package harvestmoon.items;

import static harvestmoon.helpers.CropHelper.getCropFromOrdinal;
import static harvestmoon.helpers.CropHelper.getCropFromStack;
import static harvestmoon.helpers.CropHelper.getCropQuality;
import static harvestmoon.helpers.CropHelper.getCropType;
import static harvestmoon.helpers.CropHelper.isGiant;
import static harvestmoon.lib.ModInfo.CROPPATH;
import harvestmoon.HarvestTab;
import harvestmoon.crops.Crop;
import harvestmoon.lib.CropMeta;
import harvestmoon.util.IRateable;
import harvestmoon.util.IShippable;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCrop extends ItemBaseMeta implements IShippable, IRateable {
    public ItemCrop() {
        setCreativeTab(HarvestTab.hm);
        setMaxMetaDamage(CropMeta.values().length);
        setTextureFolder(CROPPATH);
    }

    @Override
    public int getSellValue(ItemStack stack) {
        Crop crop = getCropFromStack(stack);
        boolean isGiant = isGiant(stack.getItemDamage());
        double quality = 1 + (getCropQuality(stack.getItemDamage()) * SELL_QUALITY_MODIFIER);
        int ret = (int) quality * crop.getSellValue();
        return isGiant? ret * 10: ret;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getCropFromStack(stack).getCropName(isGiant(stack.getItemDamage()));
    }

    @Override
    public int getRating(ItemStack stack) {
        int quality = getCropQuality(stack.getItemDamage());        
        return quality / 10;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int damage) {        
        return icons[getCropType(damage)];
    }
    
    @Override
    public String getName(int dmg) {
        return getCropFromOrdinal(getCropType(dmg)).getUnlocalizedName();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < meta; i++) {
            for (int j = 0; j < 100; j+=100) {
                list.add(new ItemStack(item, 1, (j * 100) + i));
            }
        }
    }
}
