package joshie.harvest.api.calendar;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public interface SeasonProvider {
    /** Return true if the hud should be displayed in the dimension **/
    @SideOnly(Side.CLIENT)
    default boolean displayHUD() {
        return true;
    }

    /** Returns the season at the position
     * Do not return null here if the displayHUD is true!!!
     * @param world the world object
     * @param pos   the block position **/
    @Nullable
    Season getSeasonAtPos(World world, @Nullable BlockPos pos);
}