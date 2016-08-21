package joshie.harvest.core.util.holders;

import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CropSoldStack extends AbstractDataHolder<CropSoldStack> {
    private final Crop crop;
    private int amount; //Amount of this item sold

    private CropSoldStack(Crop crop, int amount) {
        this.crop = crop;
        this.amount = amount;
    }

    public static CropSoldStack of(Crop crop) {
        return new CropSoldStack(crop, 1);
    }

    @Override
    public void merge(CropSoldStack stack) {
        this.amount += stack.amount;
    }

    public static CropSoldStack readFromNBT(NBTTagCompound tag) {
        Crop crop = CropRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("CropResource")));
        int amount = tag.getInteger("SellAmount");
        return new CropSoldStack(crop, amount);
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
        CropSoldStack that = (CropSoldStack) o;
        return crop != null ? crop.equals(that.crop) : that.crop == null;
    }

    @Override
    public int hashCode() {
        return crop != null ? crop.hashCode() : 0;
    }
}
