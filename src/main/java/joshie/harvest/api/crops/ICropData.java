package joshie.harvest.api.crops;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** This returns information about a planted crop **/
public interface ICropData {
    /** Returns the crop itself **/
    ICrop getCrop();

    /** Returns the stage this crop is at **/
    int getStage();
   
   /** Causes the crop to grow one stage **/
    void grow(World world, BlockPos pos);

    /** Sets this crop as having been hydrated for the day **/
    void setHydrated();
}
