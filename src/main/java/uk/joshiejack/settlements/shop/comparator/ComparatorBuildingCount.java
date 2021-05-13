package uk.joshiejack.settlements.shop.comparator;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.settlements.building.Building;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader("building_count")
public class ComparatorBuildingCount extends Comparator {
    private final Object2IntMap<Building> building_to_count = new Object2IntOpenHashMap<>();

    @Override
    public Comparator create(Row data, String id) {
        ComparatorBuildingCount comparator = new ComparatorBuildingCount();
        comparator.building_to_count.put(Building.REGISTRY.get(new ResourceLocation(data.get("building"))), data.get("multiplier"));
        return comparator;
    }

    @Override
    public void merge(Row data) {
        building_to_count.put(Building.REGISTRY.get(new ResourceLocation(data.get("building"))), data.get("multiplier"));
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        Town<?> town = TownFinder.find(target.world, target.pos);
        int count = 0;
        for (Building building: building_to_count.keySet()) {
            count += (town.getLandRegistry().getBuildingCount(building) * building_to_count.getInt(building));
        }

        return count;
    }
}
