package joshie.harvest.api.relations;

import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

@Deprecated //TODO: Remove in 0.7
public interface IRelationships {
    /** Call this to add or remove relationship points
     *  @param  player      the player to affect
     *  @param  key   the relatable whose relationship you want to affect
     *  @param  amount      how much to change the relationship by**/
    @Deprecated
    void adjustRelationship(EntityPlayer player, UUID key, int amount);

    /** Returns the relationship value between this player and the relatable
     *  This is a value from 0-65535 (by default) but can be higher based on settings
     *  @param player       the player to check
     *  @param key    the relatable to check the value of*/
    @Deprecated
    int getRelationship(EntityPlayer player, UUID key);

    @Deprecated
    int getMaximumRelationshipValue();
}