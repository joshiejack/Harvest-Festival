package joshie.harvest.api.buildings;

import joshie.harvest.api.shops.ISpecialPurchaseRules;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Building interaction **/
public interface IBuilding {
    /** This will add some special purchasing rules for the building in question
     * @param rules
     * @return the building  */
    IBuilding setSpecialRules(ISpecialPurchaseRules rules);

    /** Set the requirements for this building
     * @param requirements the building requirements **/
    IBuilding setRequirements(String... requirements);

    /** This is the ticks between a builder placing a block, defaults to 20
     * @param time the ticks*/
    IBuilding setTickTime(long time);

    /** This is the y offset to place the building at, defaults to -1
     * @param width the width of the building
     * @param heightOffset the offset, ideally use negative number
     * @param length the length of the building*/
    IBuilding setOffset(int width, int heightOffset, int length);

    /** Returns the localised name of this building **/
    String getLocalisedName();

    /** Returns the special rules for this building **/
    ISpecialPurchaseRules getRules();

    /** How much gold this building costs **/
    long getCost();

    /** How much wood this building costs **/
    int getWoodCount();

    /** How much stone this building costs **/
    int getStoneCount();

    /**Returns this building as a blueprint */
    ItemStack getBlueprint();

    /**Returns this building as a spawn building */
    ItemStack getSpawner();

    /** Generates this building at the location
     * @param world the world obj
     * @param pos   the starting position
     * @param mirror the mirroring to use
     * @param rotation the rotation to use
     * @return the result of attempting to generate a building here */
    EnumActionResult generate(World world, BlockPos pos, Mirror mirror, Rotation rotation);
}