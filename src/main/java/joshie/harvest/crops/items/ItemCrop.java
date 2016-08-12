package joshie.harvest.crops.items;

import joshie.harvest.api.core.ICreativeSorted;
import joshie.harvest.api.core.IShippable;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.base.ItemHFFML;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import joshie.harvest.crops.HFCrops;
import net.minecraft.item.ItemStack;

public class ItemCrop extends ItemHFFML<ItemCrop, Crop> implements IShippable, ICropProvider, ICreativeSorted {
    public ItemCrop() {
        super(CropRegistry.REGISTRY);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeSort.CROPS;
    }

    @Override
    public long getSellValue(ItemStack stack) {
        Crop crop = getObjectFromStack(stack);
        if (crop == HFCrops.GRASS) return 0;
        else {
            return crop.getSellValue(stack);
        }
    }

    @Override
    public ICrop getCrop(ItemStack stack) {
        return getObjectFromStack(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return getObjectFromStack(stack).getLocalizedName(true);
    }

    @Override
    public boolean shouldDisplayInCreative(Crop crop) {
        return crop.getCropStack() != null && crop.getCropStack().getItem() == this;
    }

    @Override
    public Crop getNullValue() {
        return HFCrops.NULL_CROP;
    }
}