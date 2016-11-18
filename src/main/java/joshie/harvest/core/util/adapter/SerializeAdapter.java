package joshie.harvest.core.util.adapter;

import net.minecraft.nbt.NBTTagCompound;

public abstract class SerializeAdapter<A> {
    protected A object;

    public A get() { return object; }

    public void set(A object) {
        this.object = object;
    }

    public abstract void writeToNBT(A object, NBTTagCompound tag);
    public abstract A readFromNBT(NBTTagCompound tag);
}
