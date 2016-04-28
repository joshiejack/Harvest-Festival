package joshie.harvest.api.animals;

import net.minecraft.entity.player.EntityPlayer;

/** Animals that are milkable with the milker, implement this interface **/
public interface IMilkable {
    /** Whether this animal can be milked **/
    public boolean canMilk();

    /** Perform the milking action **/
    public void milk(EntityPlayer player);
}