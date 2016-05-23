package joshie.harvest.api.crops;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface ICropData {
    /** Returns the crop itself **/
    ICrop getCrop();
    
    /** Sets this contents of this crop data 
     * @param stage **/
    ICropData setCrop(EntityPlayer player, ICrop crop, int stage);
    
    /** Returns the stage this crop is at **/
    int getStage();
   
    /** Whether or not this crop is allowed to grow **/
    boolean canGrow();

    /** Causes the crop to grow one stage **/
    void grow(World world);

    /** Sets this crop as having been hydrated for the day **/
    void setHydrated();

    /** Causes this crop to perform it's new day action
     * @return true if the crop survived without withering, false if it is withered */
    boolean newDay(World world);
    
    /** Returns the ItemStack that would be harvested
     *  @param          player The player harvesting
     *  @param          doHarvest Whether we should actually harvest the plant **/
    ItemStack harvest(EntityPlayer player, boolean doHarvest);
    
    /** Writes the crop data to nbt **/
    void readFromNBT(NBTTagCompound tag);

    /** Writes the crop data to nbt **/
    void writeToNBT(NBTTagCompound tag);
}
