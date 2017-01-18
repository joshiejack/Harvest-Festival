package joshie.harvest.api.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/** This is for creating special rules for players,
 *  for example not being able to buy something until you've married someone */
public interface ISpecialRules<E extends EntityLivingBase> {
    /** Whether this can currently be done/bought
     *  @param world    the world object
     *  @param entity   the player object
     *  @param amount   the amount of times attempting to perform this/buy this item **/
    boolean canDo(@Nonnull World world, @Nonnull E entity, int amount);
}
