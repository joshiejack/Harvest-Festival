package uk.joshiejack.husbandry.shop.comparator;

import com.google.common.collect.Lists;
import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.EntitySelectors;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;


@PenguinLoader("animal_count")
public class ComparatorAnimalCount extends Comparator {
    private List<Pair<String, Boolean>> traits;

    @Override
    public Comparator create(Row data, String id) {
        ComparatorAnimalCount comparator = new ComparatorAnimalCount();
        comparator.traits = Lists.newArrayList();
        String trait = data.get("trait");
        boolean isTrue = !trait.startsWith("!");
        comparator.traits.add(Pair.of(trait.replace("!", ""), isTrue));
        return comparator;
    }

    @Override
    public void merge(Row data) {
        String trait = data.get("trait");
        boolean isTrue = !trait.startsWith("!");
        traits.add(Pair.of(trait.replace("!", ""), isTrue));
    }

    private boolean matchesAllTraits(AnimalStats<?> stats) {
        for (Pair<String, Boolean> trait: traits) {
            if (trait.getValue() && !stats.hasTrait(trait.getKey())) return false;
            if (!trait.getValue() && stats.hasTrait(trait.getKey())) return false;
        }

        return true;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        int townID = TownFinder.find(target.world, target.pos).getID();
        return (int) target.world
                .getEntities(Objects.requireNonNull(EntityAgeable.class), EntitySelectors.IS_ALIVE)
                .stream().filter(e -> {
                    AnimalStats<?> stats = AnimalStats.getStats(e);
                    return stats != null && stats.getTown() == townID && matchesAllTraits(stats);
                }).count();
    }
}
