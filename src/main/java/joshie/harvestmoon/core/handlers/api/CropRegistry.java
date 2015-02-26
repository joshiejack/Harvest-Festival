package joshie.harvestmoon.core.handlers.api;

import joshie.harvestmoon.api.Calendar;
import joshie.harvestmoon.api.Calendar.ISeason;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.registry.ICropRegistry;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.Crop;

public class CropRegistry implements ICropRegistry {
    @Override
    public ICrop getCrop(String unlocalized) {
        for (Crop crop : Crop.crops) {
            if (crop.getUnlocalizedName().equals(unlocalized)) return crop;
        }

        return null;
    }

    @Override
    public ICrop registerCrop(String unlocalized, ISeason season, int cost, int sell, int stages, int regrow, int year, int color) {
        return new Crop(unlocalized, new Season[] { getSeasonFromISeasons(season) }, cost, sell, stages, regrow, year, color);
    }

    @Override
    public ICrop registerCrop(ICrop crop) {
        return crop;
    }

    private Season getSeasonFromISeasons(ISeason season) {
        if (season instanceof Season) return (Season) season;
        else if (season == Calendar.SPRING) return Season.SPRING;
        else if (season == Calendar.SUMMER) return Season.SUMMER;
        else if (season == Calendar.AUTUMN) return Season.AUTUMN;
        else return Season.WINTER;
    }
}
