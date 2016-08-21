package joshie.harvest.core.util.holders;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISizeable;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.core.lib.Sizeable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SizeableHolder extends AbstractItemHolder {
    private final Sizeable sizeable;

    private SizeableHolder(ISizeable sizeable) {
        this.sizeable = (Sizeable) sizeable;
    }

    public static SizeableHolder of(ISizeable sizeable) {
        return new SizeableHolder(sizeable);
    }

    @Override
    public boolean matches(ItemStack stack) {
        return HFApi.sizeable.getSizeable(stack) == sizeable;
    }

    public static SizeableHolder readFromNBT(NBTTagCompound tag) {
        return new SizeableHolder(SizeableRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("Sizeable"))));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setString("Sizeable", SizeableRegistry.REGISTRY.getNameForObject(sizeable).toString());
        return tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SizeableHolder that = (SizeableHolder) o;
        return sizeable != null ? sizeable.equals(that.sizeable) : that.sizeable == null;

    }

    @Override
    public int hashCode() {
        return sizeable != null ? sizeable.hashCode() : 0;
    }
}
