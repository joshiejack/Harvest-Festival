package joshie.harvest.api.buildings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/** This is for creating special purchasing rules for players,
 *  for example not being able to buy something until you've married someone */
public interface ISpecialPurchaseRules {
    /** Whether this can currently be bought or not **/
    boolean canBuy(World world, EntityPlayer player);
}
