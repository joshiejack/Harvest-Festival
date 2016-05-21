package joshie.harvest.core;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.api.core.IDailyTickableRegistry;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HFDailyTickable implements IDailyTickableRegistry {
    private static final TIntObjectMap<Set<IDailyTickable>> tickables = new TIntObjectHashMap<>();

    private static Set<IDailyTickable> getSet(World world) {
        Set<IDailyTickable> set = tickables.get(world.provider.getDimension());
        if (set == null) {
            set = new HashSet<>();
            tickables.put(world.provider.getDimension(), set);
        }

        return set;
    }

    public static void newDay(World world) {
        Iterator<IDailyTickable> ticking = getSet(world).iterator();
        while (ticking.hasNext()) {
            IDailyTickable tickable = ticking.next();
            if (tickable == null || tickable.isInvalid()) {
                ticking.remove();
            } else tickable.newDay(world);
        }
    }

    @Override
    public void addTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            getSet(world).add(tickable);
        }
    }

    @Override
    public void removeTickable(World world, IDailyTickable tickable) {
        if (!world.isRemote) {
            getSet(world).remove(tickable);
        }
    }
}
