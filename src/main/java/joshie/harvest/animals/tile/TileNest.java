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
    @Nonnull
    private ItemStack drop = ItemStack.EMPTY;
    private Size size;

    public void setDrop(int mother, @Nonnull ItemStack stack) {
        this.relationship = mother;
        this.drop = stack;
        this.size = HFApi.sizeable.getSize(stack);
        this.markDirty();
        markTileForUpdate(this);
    }

    public void clear() {
        this.relationship = 0;
        this.drop = ItemStack.EMPTY;
        this.size = null;
        this.markDirty();
        markTileForUpdate(this);
    }

    public int getRelationship() {
        return relationship;
    }

    @Nonnull
    public ItemStack getDrop() {
        return drop;
    }

    public Size getSize() {
        return size;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        relationship = nbt.getInteger("Relationship");
        size = Size.valueOf(nbt.getString("Size"));
        drop = new ItemStack(nbt.getCompoundTag("Drop"));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Relationship", relationship);
        nbt.setString("Size", size.name());
        nbt.setTag("Drop", drop.writeToNBT(new NBTTagCompound()));
        return super.writeToNBT(nbt);
    }
}