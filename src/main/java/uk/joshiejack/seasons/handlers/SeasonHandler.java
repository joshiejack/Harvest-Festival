package uk.joshiejack.seasons.handlers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.world.storage.AbstractWorldData;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeasonHandler {
    private final Set<Season> seasons = new HashSet<>();

    public Set<Season> getSeasons() {
        return seasons;
    }

    public boolean isValidSeason(World world, BlockPos pos) {
        List<Season> seasons = SeasonsSavedData.getWorldData(world).getSeasonAt(world, pos, AbstractWorldData.CheckMoreThanBiome.YES);
        for (Season s: seasons) {
            if (this.seasons.contains(s)) return true;
        }

        return false;
    }
}
