package uk.joshiejack.penguinlib.util.handlers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidTank;

public class MultiFluidHandler implements INBTSerializable<NBTTagCompound> {
    protected final FluidTank[] tanks;

    public MultiFluidHandler(int size, int volume) {
        this.tanks = new FluidTank[size];
        for (int i = 0; i < tanks.length; i++) {
            this.tanks[i] = new FluidTank(volume);
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (FluidTank tank : tanks) {
            NBTTagCompound tag = new NBTTagCompound();
            tank.writeToNBT(tag);
            list.appendTag(tag);
        }

        nbt.setTag("Tanks", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("Tanks", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            tanks[i].readFromNBT(tag);
        }
    }
}
