package joshie.harvest.core.util.holders;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CropHolder extends AbstractItemHolder {
    private final Crop crop;

    private CropHolder(Crop crop) {
        this.crop = crop;
    }

    public static CropHolder of(Crop crop) {
        return new CropHolder(crop);
    }

    @Override
    public List<ItemStack> getMatchingStacks() {
        matchingStacks = new ArrayList<>();
        matchingStacks.addAll(CropRegistry.INSTANCE.getStacksForCrop(crop));
        return matchingStacks;
    }

    @Override
    public boolean matches(ItemStack stack) {
        Crop container = HFApi.crops.getCropFromStack(stack);
        return container != null && container == crop;
    }

    public static CropHolder readFromNBT(NBTTagCompound tag) {
        return new CropHolder(Crop.REGISTRY.getValue(new ResourceLocation(tag.getString("CropResource"))));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (crop != null) {
            tag.setString("CropResource", crop.getRegistryName().toString());
        }

        return tag;
    }

    @Override
    public String toString() {
        return "CropHolder:" + (crop != null ? crop.getRegistryName().toString() : "null");
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
