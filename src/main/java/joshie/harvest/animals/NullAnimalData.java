package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalData;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NullAnimalData implements IAnimalData {
    @Override
    public EntityAnimal getAnimal() {
        return null;
    }

    @Override
    public EntityPlayer getOwner() {
        return null;
    }

    @Override
    public void setOwner(EntityPlayer player) {}

    @Override
    public boolean newDay() {
        return true;
    }

    @Override
    public boolean canProduce() {
        return false;
    }

    @Override
    public void setProduced() {}

    @Override
    public boolean setCleaned() {
        return false;
    }

    @Override
    public boolean setThrown() {
        return false;
    }

    @Override
    public boolean setFed() {
        return false;
    }

    @Override
    public boolean heal() {
        return false;
    }

    @Override
    public void treat(ItemStack stack, EntityPlayer player) {}

    @Override
    public boolean impregnate() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {}

    @Override
    public void writeToNBT(NBTTagCompound nbt) {}
}
