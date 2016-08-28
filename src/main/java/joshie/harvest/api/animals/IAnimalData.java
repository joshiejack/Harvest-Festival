package joshie.harvest.api.animals;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IAnimalData {
    /** Returns the animal type **/
    IAnimalType getType();

    /** Returns the instanceof this animal
     *  May return null if the animal got lost somehow **/
    EntityAnimal getAnimal();
    
    /** Returns the owner of this animal, if they are within range
     *  @return     return null if the player isn't online or there is no owner **/
    EntityPlayer getOwner();

    /** Returns the number of products this animal can produce per day **/
    int getProductsPerDay();

    /** Marks this player as the animals owner **/
    void setOwner(@Nonnull EntityPlayer player);
    
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
    void clean(@Nullable EntityPlayer player);

    /** Called when this animal is dismounted from a players head*/
    void dismount(@Nullable EntityPlayer player);

    /** Feed this animal
     *  May pass a null player if feeding is automated
     *  @param player*/
    void feed(@Nullable EntityPlayer player);

    /** Heal the animal
     *  @return     returns true if it was healed from a sickness */
    boolean heal(@Nullable EntityPlayer player);

    /** Treat the animal for this day, returns true if the animal wasn't already treated
     *  @param stack        the item used to treat the animal
     *  @param player       the player**/
    boolean treat(ItemStack stack, @Nullable EntityPlayer player);

    /** Attempt to impregnate the animal
     *  return true if sucessful */
    boolean impregnate(@Nullable EntityPlayer player);

    void setHealthiness(int healthiness);
    void setDaysNotFed(int daysNotFed);
    void setProductsProduced(boolean producedProducts);
    //Call this in your entities writeSpawnData
    void toBytes(ByteBuf buf);
    //Call this in your entities readSpawnData
    void fromBytes(ByteBuf buf);
    //Call this in your entities readEntityFromNBT
    void readFromNBT(NBTTagCompound nbt);
    //Call this in your entities writeToNBT
    void writeToNBT(NBTTagCompound nbt);
}