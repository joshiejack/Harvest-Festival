package joshie.harvest.api.player;

import net.minecraft.entity.player.EntityPlayer;

public interface IPlayerHelper {
    /** Get the stats for the player passed in
     *
     * @param player    the player */
    IPlayerStats getStatsForPlayer(EntityPlayer player);

    /** Get the relationship data for the player passed in
     *
     * @param player    the player*/
    IRelations getRelationsForPlayer(EntityPlayer player);

    /** Get the tracking data for the player passed in
     *
     * @param player    the player*/
    IPlayerTracking getTrackingForPlayer(EntityPlayer player);
}