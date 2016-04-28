package joshie.harvest.api.relations;

import net.minecraft.entity.player.EntityPlayer;

public interface IRelationships {
    /** Register a data handler **/
    public void registerDataHandler(IRelatableDataHandler handler);
    
    /** Call this whenever a player talks to something that you can have
     *  a relationship with. Only call this server side. */
    public void talkTo(EntityPlayer player, IRelatable relatable);
    
    /** Call this to add relationship points **/
    public void adjustRelationship(EntityPlayer player, IRelatable relatable, int amount);

    /** Returns the relationship value between this player and the relatable
     *  This is a value from 0-65535 */
    public int getAdjustedRelationshipValue(EntityPlayer player, IRelatable relatable);
    
    /** Returns the real number for the relationship value **/
    public short getRealRelationshipValue(EntityPlayer player, IRelatable relatable);
}