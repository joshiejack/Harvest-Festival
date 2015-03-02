package joshie.harvestmoon.core.helpers;

import joshie.harvestmoon.api.Calendar;
import joshie.harvestmoon.api.Calendar.ISeason;
import joshie.harvestmoon.calendar.Season;

public class SeasonHelper {
    public static Season[] getSeasonsFromISeasons(ISeason[] iSeasons) {
        Season[] seasons = new Season[iSeasons.length];
        for (int i = 0; i < seasons.length; i++) {
            seasons[i] = getSeasonFromISeasons(iSeasons[i]);
        }

        return seasons;
    }

    public static Season getSeasonFromISeasons(ISeason season) {
        if (season instanceof Season) return (Season) season;
        else if (season == Calendar.SPRING) return Season.SPRING;
        else if (season == Calendar.SUMMER) return Season.SUMMER;
        else if (season == Calendar.AUTUMN) return Season.AUTUMN;
        else return Season.WINTER;
    }
}
