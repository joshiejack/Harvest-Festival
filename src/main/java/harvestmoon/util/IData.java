package harvestmoon.util;

import net.minecraft.nbt.NBTTagCompound;

public interface IData {
    /** Read the nbt data **/
    public void readFromNBT(NBTTagCompound nbt);
    /** Write the nbt data **/
    public void writeToNBT(NBTTagCompound nbt);
}
