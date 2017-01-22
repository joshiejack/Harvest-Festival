package joshie.harvest.gathering;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.gathering.IGatheringRegistry;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.NavigableMap;
import java.util.TreeMap;

import static joshie.harvest.api.calendar.Season.*;

@HFApiImplementation
public class GatheringRegistry implements IGatheringRegistry {
    public static final GatheringRegistry INSTANCE = new GatheringRegistry();
    private final EnumMap<Season, WeightedState> gatherings = new EnumMap<>(Season.class);

    private GatheringRegistry() {}

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
