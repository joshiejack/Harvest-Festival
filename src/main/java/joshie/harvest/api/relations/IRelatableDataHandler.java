package joshie.harvest.api.relations;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface IRelatableDataHandler<I extends IRelatable> {
    /** The name of this data handler **/
    String name();

    /** Handling
     * @param world the world
     * @param relatable the relatable
     * @param particles to display particles or not**/
    default void onMessage(World world, I relatable, boolean particles) {}

    /** Called when reading from nbt
     * @param tag       the nbt tag to read
     * @return a new instance of this relatable**/
    I readFromNBT(NBTTagCompound tag);

    /** Write this to nbt 
     * @param relatable     the relatable
     * @param tag           the tag to write to**/
    void writeToNBT(I relatable, NBTTagCompound tag);
}