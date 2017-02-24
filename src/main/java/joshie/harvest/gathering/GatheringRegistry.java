package joshie.harvest.gathering;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.gathering.IGatheringRegistry;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.core.util.annotations.HFEvents;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

import static joshie.harvest.api.calendar.Season.*;

@HFApiImplementation
@HFEvents
public class GatheringRegistry implements IGatheringRegistry {
    public static final GatheringRegistry INSTANCE = new GatheringRegistry();
    private final EnumMap<Season, WeightedState> gatherings = new EnumMap<>(Season.class);
    private final Set<Block> gatheringStates = new HashSet<>();

    private GatheringRegistry() {
        registerValidGatheringSpawn(Blocks.GRASS);
    }

    @Override
    public void registerGathering(IBlockState state, double weight, Season... seasons) {
        if (seasons == null || seasons.length == 0) seasons = new Season[] { SPRING, SUMMER, AUTUMN, WINTER };
        for (Season season: seasons) {
            WeightedState weightedState = gatherings.get(season);
            if (weightedState == null) {
                weightedState = new WeightedState();
                gatherings.put(season, weightedState);
            }

            weightedState.add(state, weight);
        }
    }

    @Override
    public IBlockState getRandomStateForSeason(World world, @Nullable Season season) {
        if (season == null) return null;
        return gatherings.get(season).get(world);
    }

    @Override
    public void registerValidGatheringSpawn(Block block) {
        gatheringStates.add(block);
    }

    public boolean isValidGatheringSpawn(Block block) {
        return gatheringStates.contains(block);
    }

    private class WeightedState {
        private final NavigableMap<Double, IBlockState> map = new TreeMap<>();
        private double total = 0;

        public void add(IBlockState state, double weight) {
            if (weight <= 0) return;
            total += weight;
            map.put(total, state);
        }

        public IBlockState get(World world) {
            return map.ceilingEntry((world.rand.nextDouble() * total)).getValue();
        }
    }
}
