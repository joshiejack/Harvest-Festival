package joshie.harvest.api.relations;

import net.minecraft.entity.player.EntityPlayer;

public interface IRelationships {
    /** Register a data handler **/
    void registerDataHandler(IRelatableDataHandler handler);

    IRelatableDataHandler getDataHandler(String name);

    /** Call this to add or remove relationship points
     *  @param  player      the player to affect
     *  @param  relatable   the relatable whose relationship you want to affect
     *  @param  amount      how much to change the relationship by**/
    void adjustRelationship(EntityPlayer player, IRelatable relatable, int amount);

    /** Returns the relationship value between this player and the relatable
     *  This is a value from 0-65535 (by default) but can be higher based on settings
     *  @param player       the player to check
     *  @param relatable    the relatable to check the value of*/
    int getRelationship(EntityPlayer player, IRelatable relatable);

    int getMaximumRelationshipValue();
}