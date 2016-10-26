package joshie.harvest.api.mining;

import joshie.harvest.api.calendar.Season;
import net.minecraft.block.state.IBlockState;

public interface IMiningRegistry {
    /** Register a block state to be generated in a specific season
     * @param state the block state to generate
     * @param weight weight for this to generate
     * @param seasons the season this should generate in,
     *                leave this blank if it should generate
     *                in every single season. **/
    void registerOre(MiningContext context, IBlockState state, double weight, Season... seasons);
}
