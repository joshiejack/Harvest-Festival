package joshie.harvest.api.animals;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface IAnimalData {
    /** @return the animal type that this data is tracking **/
    IAnimalType getType();

    /** Returns the owner of this animal, if they are within range
     *  @return     return null if the player isn't online, no owner or not withing 128 blocks **/
    @Nullable
    EntityPlayer getOwner();

    /** @return the maximum number of products this animal can produce per day **/
    int getProductsPerDay();

    /** Marks this player as the animals owner **/
    void setOwner(@Nonnull UUID uuid);

    /** @return true if this animal is hungry **/
    boolean isHungry();

    /** @return  true if this animal can currently produce products **/
    boolean canProduce();

    /** Mark this animal as having produced a product today
     * @param amount the amount to count as having produced**/
    void setProduced(int amount);

    /** Mark this animal as having been died
     *  The data for it will be removed when the day ticks over */
    void setDead();

    /** Clean this animal
     * @param player    the player cleaning the animal */
    void clean(@Nullable EntityPlayer player);

    /** Called when this animal is dismounted from a players head
     * @param player    the player the animal was riding */
    void dismount(@Nullable EntityPlayer player);

    /** Feed this animal
     *  May pass a null player if feeding is automated
     *  @param player   the player */
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

    /** IMPORTANT:!!!::::::!;;;;;;;;; ****/
    //Call this in your entities writeSpawnData
    void toBytes(ByteBuf buf);
    //Call this in your entities readSpawnData
    void fromBytes(ByteBuf buf);
    //Call this in your entities readEntityFromNBT
    void readFromNBT(NBTTagCompound nbt);
    //Call this in your entities writeToNBT
    void writeToNBT(NBTTagCompound nbt);
}