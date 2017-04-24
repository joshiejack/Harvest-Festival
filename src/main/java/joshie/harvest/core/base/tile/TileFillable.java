package joshie.harvest.core.base.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.animals.AnimalFoodType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public abstract class TileFillable extends TileHarvest {
    private final AnimalFoodType foodType;
    private final int maxFill;
    private final int fillPer;
    protected int fillAmount;

    public TileFillable(AnimalFoodType type, int max, int per) {
        foodType = type;
        maxFill = max;
        fillPer = per;
    }

    protected TileFillable getTile() {
        return this;
    }

    public boolean onActivated(@Nonnull ItemStack held) {
        if (HFApi.animals.canEat(held, foodType) && held.getCount() > 0) {
            TileFillable fillable = getTile();
            if (fillable != null) {
                if (fillable.getFillAmount() + fillPer <= fillable.getMaximumFill() && fillable.setFilled(fillable.getFillAmount() + fillPer)) {
                    held.splitStack(1);
                    return true;
                }
            }
        }

        return false;
    }

    public int getFillAmount() {
        return fillAmount;
    }

    public int getMaximumFill() {
        return maxFill;
    }

    public void adjustFill(int amount) {
        setFilled(fillAmount + amount);
    }

    public boolean setFilled(int isFilled) {
        fillAmount = Math.min(getMaximumFill(), isFilled);
        saveAndRefresh();
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        fillAmount = nbt.getByte("IsFilled");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setByte("IsFilled", (byte) fillAmount);
        return super.writeToNBT(nbt);
    }
}
