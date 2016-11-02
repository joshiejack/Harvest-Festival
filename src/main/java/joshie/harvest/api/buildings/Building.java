package joshie.harvest.api.buildings;

import joshie.harvest.api.core.ISpecialPurchaseRules;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Building interaction **/
public interface Building {
    /** This will add some special purchasing rules for the building in question
     * @param rules the special rules
     * @return the building  */
    Building setSpecialRules(ISpecialPurchaseRules rules);

    /** Set the requirements for this building
     * @param requirements the building requirements **/
    Building setRequirements(String... requirements);

    /** This is the ticks between a builder placing a block, defaults to 20
     * @param time the ticks*/
    Building setTickTime(long time);

    /** This is the y offset to place the building at, defaults to -1
     * @param width the width of the building
     * @param heightOffset the offset, ideally use negative number
     * @param length the length of the building*/
    Building setOffset(int width, int heightOffset, int length);

    /** Allows multiple of this building **/
    Building setMultiple();

    /** Mark a building as not purchaseable **/
    Building setNoPurchase();

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
     * @param rotation the rotation to use
     * @return the result of attempting to generate a building here */
    EnumActionResult generate(World world, BlockPos pos, Rotation rotation);

    @Deprecated //TODO: Remove in 0.7+
    EnumActionResult generate(World world, BlockPos pos, Mirror mirror, Rotation rotation);
}