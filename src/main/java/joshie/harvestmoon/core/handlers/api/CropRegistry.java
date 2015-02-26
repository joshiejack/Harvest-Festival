package joshie.harvestmoon.core.handlers.api;

import joshie.harvestmoon.api.Calendar;
import joshie.harvestmoon.api.Calendar.ISeason;
import joshie.harvestmoon.api.interfaces.ICrop;
import joshie.harvestmoon.api.registry.ICropRegistry;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.Crop;

public class CropRegistry implements ICropRegistry {    
    @Override
    public ICrop registerCrop(String unlocalized, ISeason season, int cost, int sell, int stages, int regrow, int year, int color) {
        return registerCrop(unlocalized, new ISeason[] { season }, cost, sell, stages, regrow, year, color);
    }

    @Override
    public ICrop registerCrop(String unlocalized, ISeason[] seasons, int cost, int sell, int stages, int regrow, int year, int color) {
        return new Crop(unlocalized, ISeasonToSeasons(seasons), cost, sell, stages, regrow, year, color);
    }

    private Season[] ISeasonToSeasons(ISeason[] iSeasons) {
        Season[] seasons = new Season[iSeasons.length];
        for (int i = 0; i < seasons.length; i++) {
            seasons[i] = getSeasonFromISeasons(iSeasons[i]);
        }

        return seasons;
    }

    private Season getSeasonFromISeasons(ISeason season) {
        if (season instanceof Season) return (Season) season;
        else if (season == Calendar.SPRING) return Season.SPRING;
        else if (season == Calendar.SUMMER) return Season.SUMMER;
        else if (season == Calendar.AUTUMN) return Season.AUTUMN;
        else return Season.WINTER;
    }
}
