package uk.joshiejack.harvestcore.registry;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.util.PenguinRegistry;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.handlers.SeasonHandler;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class Holiday extends PenguinRegistry {
    public static final Map<ResourceLocation, Holiday> REGISTRY = Maps.newHashMap();
    public static final int LENGTH = 0; //How many days this lasts//TODO: Change when the world is a server to be * 10
    private final int day;
    private final SeasonHandler season;

    private  <T extends PenguinRegistry> Holiday(ResourceLocation resource, int day, SeasonHandler season) {
        super(REGISTRY, resource);
        this.day = day;
        this.season = season;
    }

    public int getDay() {
        return day;
    }

    public boolean isSeason(Season s) {
        for (Season season: this.season.getSeasons()) {
            return season == s;
        }

        return false;
    }

    public static void create(int day, SeasonHandler season, String name) {
        new Holiday(new ResourceLocation(name), day, season);
    }
}
