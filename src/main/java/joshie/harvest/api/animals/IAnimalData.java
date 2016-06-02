package joshie.harvest.api.animals;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IAnimalData {
    /** Returns the instanceof this animal
     *  May return null if the animal got lost somehow **/
    EntityAnimal getAnimal();
    
    /** Returns the owner of this animal, if they are within range
     *  @return     return null if the player isn't online or there is no owner **/
    EntityPlayer getOwner();

    /** Returns the number of products this animal can produce per day **/
    int getProductsPerDay();

    /** Marks this player as the animals owner **/
    void setOwner(EntityPlayer player);
    
    /** Call to check whether the animal is hungry or not **/
    boolean isHungry();

    /** Returns true if this animal recently died **/
    boolean hasDied();

    /** Called when a new day passes for this animal
     *  should return false if this change causes the animal to die.*/
    boolean newDay();

    /** Returns true if this animal can currently produce products **/
    boolean canProduce();

    /** Mark this animal as having produced a product today **/
    void setProduced();

    /** Clean this animal */
    void clean(EntityPlayer player);

    /** Called when this animal is dismounted from a players head*/
    void dismount(EntityPlayer player);

    /** Feed this animal
     *  May pass a null player if feeding is automated */
    void feed(EntityPlayer player);

    /** Heal the animal
     *  @return     returns true if it was healed from a sickness */
    boolean heal(EntityPlayer player);

    /** Treat the animal for this day, returns true if the animal wasn't already treated **/
    boolean treat(ItemStack stack, EntityPlayer player);

    /** Attempt to impregnate the animal
     *  return true if sucessful */
    boolean impregnate(EntityPlayer player);

    /** Syncs any important data to the connected clients **/
    void toBytes(ByteBuf buf);
    void fromBytes(ByteBuf buf);

    /** Read information from nbt **/
    void readFromNBT(NBTTagCompound nbt);

    /** Write information to nbt **/
    void writeToNBT(NBTTagCompound nbt);

    /** Setters **/
    void setHealthiness(byte healthiness);
    void setDaysNotFed(byte daysNotFed);
    void setProductsProduced(boolean producedProducts);
}