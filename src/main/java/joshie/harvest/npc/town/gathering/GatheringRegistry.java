package joshie.harvest.npc.town.gathering;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.gathering.IGatheringRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

import java.util.EnumMap;
import java.util.NavigableMap;
import java.util.TreeMap;

public class GatheringRegistry implements IGatheringRegistry {
    private final EnumMap<Season, WeightedState> gatherings = new EnumMap<>(Season.class);

    public GatheringRegistry() {
        gatherings.put(Season.SPRING, new WeightedState());
        gatherings.put(Season.SUMMER, new WeightedState());
        gatherings.put(Season.AUTUMN, new WeightedState());
        gatherings.put(Season.WINTER, new WeightedState());
    }

    @Override
    public void registerGathering(Season season, IBlockState state, double weight) {
        gatherings.get(season).add(state, weight);
    }

    @Override
    public void registerGathering(IBlockState state, double weight) {
        for (Season season: Season.values()) {
            registerGathering(season, state, weight);
        }
    }

    @Override
    public IBlockState getRandomStateForSeason(World world, Season season) {
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
