package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IAnimalData {
    /** Returns the instanceof this animal
     *  May return null if the animal got lost somehow **/
    public EntityAnimal getAnimal();
    
    /** Returns the owner of this animal, if they are within range
     *  @return     return null if the player isn't online or there is no owner **/
    public EntityPlayer getOwner();

    /** Marks this player as the animals owner **/
    public void setOwner(EntityPlayer player);
    
    /** Call to check whether the animal is hungry or not **/
    public boolean isHungry();

    /** Called when a new day passes for this animal
     *  should return false if this change causes the animal to die.*/
    public boolean newDay();

    /** Returns true if this animal can currently produce products **/
    public boolean canProduce();

    /** Mark this animal as having produced a product today **/
    public void setProduced();

    /** Clean this animal */
    public void clean(EntityPlayer player);

    /** Called when this animal is dismounted from a players head*/
    public void dismount(EntityPlayer player);

    /** Feed this animal
     *  May pass a null player if feeding is automated */
    public void feed(EntityPlayer player);

    /** Heal the animal
     *  @return     returns true if it was healed from a sickness */
    public boolean heal(EntityPlayer player);

    /** Treat the animal for this day **/
    public void treat(ItemStack stack, EntityPlayer player);

    /** Attempt to impregnate the animal
     *  return true if sucessful */
    public boolean impregnate(EntityPlayer player);

    /** Read information from nbt **/
    public void readFromNBT(NBTTagCompound nbt);

    /** Write information to nbt **/
    public void writeToNBT(NBTTagCompound nbt);
}
