package joshie.harvest.api.crops;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IGrowthHandler {
    /** Add tooltips to the crops
     *  @param list         the tooltiplist
     *  @param crop         the crop
     *  @param debug        debug mode? **/
    @SideOnly(Side.CLIENT)
    void addInformation(List<String> list, ICrop crop, boolean debug);

    /** Returns true if this crop can stay on this soil
     * @param world         the world
     * @param pos           the position
     * @param state         the state
     * @param crop          the crop  **/
    boolean canSustainCrop(IBlockAccess world, BlockPos pos, IBlockState state, ICrop crop);

    /** Return true if this crop can't crop in this location
     *  @param world        the world
     *  @param pos          the position
     *  @param crop         the crop **/
    boolean canGrow(World world, BlockPos pos, ICrop crop);
}