package joshie.harvest.api.crops;

import joshie.harvest.api.calendar.Season;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;

public interface ICropHandler {
    public ICrop registerCrop(String unlocalized, int cost, int sell, int stages, int regrow, int year, int color, Season... seasons);

    public ICrop registerCrop(ICrop crop);

    public ICrop getCrop(String unlocalized);
    
    /** Will NEVER return null, however it may have an instance of 'null_crop' **/
    public ICropData getCropAtLocation(World world, BlockPos pos);

    /** Returns a collection of all registered crops **/
    public Collection<ICrop> getCrops();
}