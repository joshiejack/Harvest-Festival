package uk.joshiejack.seasons.world.storage;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.data.database.LocationBasedSeasonRegistry;
import uk.joshiejack.seasons.world.weather.Weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import static uk.joshiejack.seasons.world.weather.Weather.CLEAR;

public class AbstractWorldData {
    public static final EnumMap<Season, List<Season>> seasons = new EnumMap<>(Season.class);
    protected Weather weather = CLEAR;
    protected Season season = Season.SPRING;
    static {
        for (Season season: Season.values()) {
            seasons.put(season, Lists.newArrayList(season));
        }
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Season getSeason() {
        return season;
    }

    public Season fromBiome(Biome biome) {
        return fromBiomeOr(season, biome);
    }

    public static Season fromBiomeOr(Season season, Biome biome) {
        //Savanna, Wet season = Summer
        //Desert/Mesa, Wet season = Winter
        //Jungle, Dry season = Summer/Autumn
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) ? season == Season.SUMMER ? Season.WET: Season.DRY :
                BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) ? season == Season.WINTER ? Season.WET : Season.DRY :
                        BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) ? season == Season.SUMMER || season == Season.AUTUMN ? Season.DRY : Season.WET :
                                season;
    }

    public enum CheckMoreThanBiome {
        YES, NO
    }

    public List<Season> getSeasonAt(World world, BlockPos pos, CheckMoreThanBiome check) {
        if (check == CheckMoreThanBiome.YES) {
            List<Season> s = new ArrayList<>();
            for (Interpreter locational : LocationBasedSeasonRegistry.getSeasonHandlers()) {
                Object seasons = locational.getResultOfFunction("getSeasons", world, pos);
                if (seasons instanceof Season[]) {
                    Collections.addAll(s, (Season[]) seasons);
                }
            }

            if (!s.isEmpty()) {
                return s;
            }
        }

        return world.canSnowAt(pos, false) ? seasons.get(Season.WINTER) : seasons.get(fromBiome(world.getBiome(pos)));
    }

    public Weather getWeather() {
        return weather;
    }
}
