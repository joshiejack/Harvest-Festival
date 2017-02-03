package joshie.harvest.animals.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.base.tile.TileHarvest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.UUID;

import static joshie.harvest.core.helpers.MCServerHelper.markTileForUpdate;

public class TileNest extends TileHarvest {
    private UUID mother;
    private ItemStack drop;
    private Size size;

    public void setDrop(UUID mother, ItemStack stack) {
        this.mother = mother;
        this.drop = stack;
        this.size = HFApi.sizeable.getSize(stack);
        this.markDirty();
        markTileForUpdate(this);
    }

    public void clear() {
        this.mother = null;
        this.drop = null;
        this.size = null;
        this.markDirty();
        markTileForUpdate(this);
    }

    public UUID getMother() {
        return mother;
    }

    public ItemStack getDrop() {
        return drop;
    }

    public Size getSize() {
        return size;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("Mother")) mother = UUID.fromString(nbt.getString("Mother"));
        if (nbt.hasKey("Size")) size = Size.valueOf(nbt.getString("Size"));
        if (nbt.hasKey("Drop")) {
            drop = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Drop"));
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (mother != null) nbt.setString("Mother", mother.toString());
        if (size != null) nbt.setString("Size", size.name());
        if (drop != null) nbt.setTag("Drop", drop.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(nbt);
    }
}