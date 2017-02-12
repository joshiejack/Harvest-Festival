package joshie.harvest.api.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/** This is for creating special rules for players,
 *  for example not being able to buy something until you've married someone */
public interface ISpecialRules {
    /** Whether this can currently be done/bought
     *  @param world    the world object
     *  @param player   the player object
     *  @param amount   the amount of times attempting to perform this/buy this item **/
    boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount);
}
