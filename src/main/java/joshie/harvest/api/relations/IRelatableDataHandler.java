package joshie.harvest.api.relations;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public interface IRelatableDataHandler {
    /** Copy cat **/
    public IRelatableDataHandler copy();
    
    /** The name of this data handler **/
    public String name();

    /** Write this to bytebuf **/
    public void toBytes(IRelatable relatable, ByteBuf buf);

    /** Read this from bytebuf **/
    public void fromBytes(ByteBuf buf);
    
    /** Handling **/
    public IRelatable onMessage(boolean particles);

    /** Called when reading from nbt **/
    public IRelatable readFromNBT(NBTTagCompound tag);

    /** Write this to nbt 
     * @param relatable **/
    public void writeToNBT(IRelatable relatable, NBTTagCompound tag);
}
