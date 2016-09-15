package joshie.harvest.api.crops;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface ICropRegistry {
    /** Alternative if you don't want to implement ICropProvider **/
    void registerCropProvider(ItemStack stack, Crop crop);

    /** Return this crop this stack provides, or null if it provides none
     *  @param stack the item stack to check
     *  @return the ICrop**/
    Crop getCropFromStack(ItemStack stack);

    /** Returns the crop as seeds **/
    ItemStack getSeedStack(Crop crop, int amount);

    /** Fetch the crop at this location, will return null if there is no crop there
     *  @param world the world
     *  @param pos the block position
     *  @return the crop data the loation**/
    Crop getCropAtLocation(World world, BlockPos pos);

    /** Called to plant a crop at the location, the location should be location the crop itself
     *
     * @param player the player planting the crop, used for tracking stats, can be null
     * @param world the world play
     * @param pos the position you planting, this should be the position of the crop itself
     * @param theCrop the crop you are planting
     * @param stage the growth stage of the plant */
    void plantCrop(@Nullable EntityPlayer player, World world, BlockPos pos, Crop theCrop, int stage);

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

    /** Helper method for getting the correct state container
     * @param stages  the maximum number of stages
     * @return the state container  */
    BlockStateContainer getStateContainer(PropertyInteger stages);
}