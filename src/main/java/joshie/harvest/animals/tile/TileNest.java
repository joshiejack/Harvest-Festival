package joshie.harvest.animals.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.Size;
import joshie.harvest.core.base.tile.TileHarvest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

import static joshie.harvest.core.helpers.MCServerHelper.markTileForUpdate;

public class TileNest extends TileHarvest {
    private int relationship;
    private ItemStack drop;
    private Size size;

    public void setDrop(int mother, ItemStack stack) {
        this.relationship = mother;
        this.drop = stack;
        this.size = HFApi.sizeable.getSize(stack);
        this.markDirty();
        markTileForUpdate(this);
    }

    public void clear() {
        this.relationship = 0;
        this.drop = null;
        this.size = null;
        this.markDirty();
        markTileForUpdate(this);
    }

    public int getRelationship() {
        return relationship;
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
        if (nbt.hasKey("Relationship")) relationship = nbt.getInteger("Relationship");
        if (nbt.hasKey("Size")) size = Size.valueOf(nbt.getString("Size"));
        if (nbt.hasKey("Drop")) {
            drop = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Drop"));
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (relationship != 0) nbt.setInteger("Relationship", relationship);
        if (size != null) nbt.setString("Size", size.name());
        if (drop != null) nbt.setTag("Drop", drop.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(nbt);
    }
}