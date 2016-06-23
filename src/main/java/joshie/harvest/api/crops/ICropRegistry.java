package joshie.harvest.api.crops;

import joshie.harvest.api.calendar.Season;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ICropRegistry {
    /** Registers a crop with some specific information **/
    ICrop registerCrop(ResourceLocation resource, int cost, int sell, int stages, int regrow, int year, int color, Season... seasons);

    /** Returns a crop from the resource location **/
    ICrop getCrop(ResourceLocation resource);

    /** Alternative if you don't want to implement ICropProvider **/
    ICrop registerCropProvider(ItemStack stack, ICrop crop);

    /** Return this crop this stack provides, or null if it provides none
     *  @param stack the item stack to check
     *  @return the ICrop**/
    ICrop getCropFromStack(ItemStack stack);

    /** Fetch the crop at this location, will return null if there is no crop there
     *  @param world the world
     *  @param pos the block position
     *  @return the crop data the loation**/
    ICropData getCropAtLocation(World world, BlockPos pos);

    /** Called to plant a crop at the location, the location should be location the crop itself
     *
     * @param player the player planting the crop, used for tracking stats, can be null
     * @param world the world play
     * @param pos the position you planting, this should be the position of the crop itself
     * @param theCrop the crop you are planting
     * @param stage the growth stage of the plant */
    void plantCrop(@Nullable EntityPlayer player, World world, BlockPos pos, ICrop theCrop, int stage);

    /** Call this to harvest a crap at the location
     *
     * @param player the player harvesting, can be null
     * @param world the world object
     * @param pos the crop position
     * @return the result of harvesting this crop */
    ItemStack harvestCrop(@Nullable EntityPlayer player, World world, BlockPos pos);

    /** Call this on blocks of soil to hydrate the soil, and the plant. Make sure you pass in the position of the soil
     * @param player  the player making the soil wet, can be null
     * @param world the world
     * @param pos the position of the soil
     * @return whether any water was used or not  */
    boolean hydrateSoil(@Nullable EntityPlayer player, World world, BlockPos pos);
}