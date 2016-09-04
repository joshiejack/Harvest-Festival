package joshie.harvest.player.tracking;

import joshie.harvest.core.util.holder.AbstractDataHolder;
import joshie.harvest.api.crops.Crop;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CropHarvested extends AbstractDataHolder<CropHarvested> {
    private final Crop crop;
    private int amount; //Amount of this item sold

    private CropHarvested(Crop crop, int amount) {
        this.crop = crop;
        this.amount = amount;
    }

    public static CropHarvested of(Crop crop) {
        return new CropHarvested(crop, 1);
    }

    @Override
    public void merge(CropHarvested stack) {
        this.amount += stack.amount;
    }

    public static CropHarvested readFromNBT(NBTTagCompound tag) {
        Crop crop = Crop.REGISTRY.getValue(new ResourceLocation(tag.getString("CropResource")));
        int amount = tag.getInteger("SellAmount");
        return new CropHarvested(crop, amount);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (crop != null) {
            tag.setString("CropResource", crop.getRegistryName().toString());
            tag.setInteger("SellAmount", amount);
        }

        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CropHarvested that = (CropHarvested) o;
        return crop != null ? crop.equals(that.crop) : that.crop == null;
    }

    @Override
    public int hashCode() {
        return crop != null ? crop.hashCode() : 0;
    }
}
