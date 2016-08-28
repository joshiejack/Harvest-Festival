package joshie.harvest.core.util.holder;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class CropHolder extends AbstractItemHolder {
    private final Crop crop;

    private CropHolder(Crop crop) {
        this.crop = crop;
    }

    public static CropHolder of(Crop crop) {
        return new CropHolder(crop);
    }

    @Override
    public boolean matches(ItemStack stack) {
        ICrop container = HFApi.crops.getCropFromStack(stack);
        if (container != null) {
            return container == crop;
        }

        return false;
    }


    public static CropHolder readFromNBT(NBTTagCompound tag) {
        return new CropHolder(CropRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("CropResource"))));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (crop != null) {
            tag.setString("CropResource", crop.getRegistryName().toString());
        }

        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CropHolder that = (CropHolder) o;
        return crop != null ? crop.equals(that.crop) : that.crop == null;

    }

    @Override
    public int hashCode() {
        return crop != null ? crop.hashCode() : 0;
    }
}
