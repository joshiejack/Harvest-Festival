package joshie.harvest.api.relations;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public interface IRelatableDataHandler<I extends IRelatable> {
    /** Copy cat **/
    IRelatableDataHandler copy();
    
    /** The name of this data handler **/
    String name();

    /** Write this to bytebuf **/
    void toBytes(I relatable, ByteBuf buf);

    /** Read this from bytebuf **/
    I fromBytes(ByteBuf buf);
    
    /** Handling **/
    default void onMessage(I relatable, boolean particles) {}

    /** Called when reading from nbt
     * @param tag       the nbt tag to read
     * @return a new instance of this relatable**/
    I readFromNBT(NBTTagCompound tag);

    /** Write this to nbt 
     * @param relatable     the relatable
     * @param tag           the tag to write to**/
    void writeToNBT(I relatable, NBTTagCompound tag);
}