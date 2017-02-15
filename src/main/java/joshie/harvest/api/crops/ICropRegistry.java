package joshie.harvest.api.crops;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public interface ICropRegistry {
    /** Alternative if you don't want to implement ICropProvider
     * @param stack     the item
     * @param crop      the crop this provides**/
    void registerCropProvider(ItemStack stack, Crop crop);

    /** Return this crop this stack provides, or null if it provides none
     *  @param stack the item stack to check
     *  @return the ICrop**/
    Crop getCropFromStack(ItemStack stack);

    /** Returns the crop as seeds **/
    ItemStack getSeedStack(Crop crop, int amount);

    /** Register a seed, so that if the config disables it
     *  the seed cannot be right clicked
     *  @param item     the seeds */
    void registerSeedForBlacklisting(ItemStack item);

    /** Register a block, that would normally drop seeds
     *  In order to prevent it from dropping any blacklisted seeds
     *  @param block    the block*/
    void registerBlockForSeedRemoval(Block block);

    /** Fetch the crop at this location, will return null if there is no crop there
     *  @param world the world
     *  @param pos the block position
     *  @return the crop data the loation**/
    @Nullable
    @SuppressWarnings("unused")
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
    List<ItemStack> harvestCrop(@Nullable EntityPlayer player, World world, BlockPos pos);

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

    /** Register a watering handler, these are called to check
     *  if something is considered water, and also by the watering can
     *  and sprinkler in order to turn the blocks in to the correct form
     *  @param handler  the watering handler **/
    void registerWateringHandler(WateringHandler handler);

    /** Register a farmland to dirt mapping
     *  @param farmland     the farmland block
     *  @param dirt         the block this becomes when it 'dies'**/
    void registerFarmlandToDirtMapping(IBlockState farmland, IBlockState dirt);
}