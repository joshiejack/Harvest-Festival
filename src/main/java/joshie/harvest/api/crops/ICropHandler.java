package joshie.harvest.api.crops;

import joshie.harvest.api.calendar.Season;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;

public interface ICropHandler {
    /** Registers a crop with some specific information **/
    public ICrop registerCrop(ResourceLocation resource, int cost, int sell, int stages, int regrow, int year, int color, Season... seasons);

    /** Registers a crop, ensure that your crop returns the correct getResource() **/
    public ICrop registerCrop(ICrop crop);

    /** Returns a crop from the resource location **/
    public ICrop getCrop(ResourceLocation resource);
    
    /** Will NEVER return null, however it may have an instance of 'null_crop' **/
    public ICropData getCropAtLocation(World world, BlockPos pos);

    /** Returns a collection of all registered crops **/
    public Collection<ICrop> getCrops();

    /** Alternative if you don't want to implement ICropProvider **/
    public ICrop registerCropProvider(ItemStack stack, ICrop crop);

    /** Return this crop this stack provides, or null if it provides none **/
    public ICrop getCropFromStack(ItemStack stack);
}