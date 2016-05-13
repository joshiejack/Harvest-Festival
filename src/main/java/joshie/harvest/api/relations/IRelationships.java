package joshie.harvest.api.relations;

import net.minecraft.entity.player.EntityPlayer;

public interface IRelationships {
    /** Register a data handler **/
    void registerDataHandler(IRelatableDataHandler handler);

    IRelatableDataHandler getDataHandler(String name);
    
    /** Call this whenever a player talks to something that you can have
     *  a relationship with. Only call this server side. */
    void talkTo(EntityPlayer player, IRelatable relatable);
    
    /** Call this to add relationship points **/
    void adjustRelationship(EntityPlayer player, IRelatable relatable, int amount);

    /** Returns the relationship value between this player and the relatable
     *  This is a value from 0-65535 (by default) but can be higher based on settings */
    int getAdjustedRelationshipValue(EntityPlayer player, IRelatable relatable);

    int getMaximumRelationshipValue();
}