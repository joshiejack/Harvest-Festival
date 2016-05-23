package joshie.harvest.api.relations;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public interface IRelatableDataHandler {
    /** Copy cat **/
    IRelatableDataHandler copy();
    
    /** The name of this data handler **/
    String name();

    /** Write this to bytebuf **/
    void toBytes(IRelatable relatable, ByteBuf buf);

    /** Read this from bytebuf **/
    void fromBytes(ByteBuf buf);
    
    /** Handling **/
    IRelatable onMessage(boolean particles);

    /** Called when reading from nbt **/
    IRelatable readFromNBT(NBTTagCompound tag);

    /** Write this to nbt 
     * @param relatable **/
    void writeToNBT(IRelatable relatable, NBTTagCompound tag);
}