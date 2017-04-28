package joshie.harvest.mining;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.mining.IMiningRegistry;
import joshie.harvest.api.mining.MiningContext;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;

import static joshie.harvest.api.calendar.Season.*;

@HFApiImplementation
public class MiningRegistry implements IMiningRegistry {
    public static final MiningRegistry INSTANCE = new MiningRegistry();
    private final EnumMap<Season, WeightedState> gatherings = new EnumMap<>(Season.class);
    public final Set<IBlockState> all = new HashSet<>();

    private MiningRegistry() {}

    @Override
    public void registerOre(MiningContext context, IBlockState state, double weight, Season... seasons) {
        if (seasons == null || seasons.length == 0) seasons = new Season[] { SPRING, SUMMER, AUTUMN, WINTER };
        for (Season season: seasons) {
            WeightedState weightedState = gatherings.computeIfAbsent(season, k -> new WeightedState());

            weightedState.add(context, state, weight);
        }

        all.add(state);
    }

    @Nullable
    public IBlockState getRandomStateForSeason(World world, int floor, @Nullable Season season) {
        if (season == null) season = Season.SPRING;
        return gatherings.get(season).get(world, floor);
    }

    private class WeightedState {
        private final NavigableMap<Double, Pair<IBlockState, MiningContext>> map = new TreeMap<>();

        private double total = 0;

        public void add(MiningContext context, IBlockState state, double weight) {
            if (weight <= 0) return;
            total += weight;
            map.put(total, Pair.of(state, context));
        }

        @Nullable
        public IBlockState get(World world, int floor) {
            Pair<IBlockState, MiningContext> pair = map.ceilingEntry((world.rand.nextDouble() * total)).getValue();
            if (pair.getValue().isValidFloor(floor)) {
                return pair.getKey();
            } else return null;
        }
    }
}
