package joshie.harvest.core.util.holders;

import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractHolder {
    public abstract NBTTagCompound writeToNBT(NBTTagCompound tag);
}
