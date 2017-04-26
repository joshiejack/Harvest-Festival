package joshie.harvest.api.shops;

import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.core.ISpecialRules;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface OpeningHandler {
    /** If the Shop is currently open
     * @param world the world object
     * @param npc   the npc entity
     * @param player the player entity
     * @param shop the shop
     * @return true if open  */
    boolean isOpen(World world, EntityAgeable npc, @Nullable EntityPlayer player, Shop shop);

    /** Add an opening time
     * @param day   day of the week
     * @param opening hours opened in 24 hour format ie 6000 for a 6am start
     * @param closing hours closed at in 24 hour format ie 17000 for a 5pm finish
     * @param rules an optional field, only ever pass one  */
    void addOpening(Weekday day, int opening, int closing, ISpecialRules... rules);

    /** Allows this shop to open on holidays */
    OpeningHandler setOpensOnHolidays();
}