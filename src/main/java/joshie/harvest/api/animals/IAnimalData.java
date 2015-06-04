package joshie.harvest.api.animals;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IAnimalData {
    /** Returns the instanceof this animal
     *  May return null if the animal got lost somehow **/
    public EntityAnimal getAnimal();
    
    /** Returns the owner of this animal, if they are within range
     *  @return     return null if the player isn't online or there is no owner **/
    public EntityPlayer getOwner();

    /** Marks this player as the animals owner **/
    public void setOwner(EntityPlayer player);

    /** Called when a new day passes for this animal
     *  should return false if this change causes the animal to die.*/
    public boolean newDay();

    /** Returns true if this animal can currently produce products **/
    public boolean canProduce();

    /** Mark this animal as having produced a product today **/
    public void setProduced();

    /** Mark this animal as having been cleaned,
     *  return true if the animal wasn't fully clean beforehand. */
    public boolean setCleaned();

    /** Marks the animal as having been thrown this day
     *  If it has already been thrown, return false */
    public boolean setThrown();

    /** Attempt to feed the animal for this day,
     *  If it's already been fed, return false  */
    public boolean setFed();

    /** Attempt to heal the animal
     *  Return false if the animal was already fully healed */
    public boolean heal();

    /** Treat the animal for this day **/
    public void treat(ItemStack stack, EntityPlayer player);

    /** Attempt to impregnate the animal
     *  return true if sucessful */
    public boolean impregnate();
}
