package joshie.harvest.plugins.crafttweaker.handlers;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.plugins.crafttweaker.CraftTweaker;
import joshie.harvest.plugins.crafttweaker.base.BaseUndoable;
import minetweaker.MineTweakerAPI;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import javax.annotation.Nullable;

@ZenClass("mods.harvestfestival.Seasons")
public class Seasons {
    @Nullable
    private static Season getSeasonFromString(String string) {
        return Season.valueOf(string.toUpperCase());
    }

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setSunrise(String season, int time) {
        Season theSeason = getSeasonFromString(season);
        if (theSeason == null) CraftTweaker.logError("No season named " + season + " could be found");
        else MineTweakerAPI.apply(new SetSunrise(theSeason, time));
    }

    private static class SetSunrise extends BaseUndoable {
        private final Season season;
        private final int time;
        private int original;

        SetSunrise(Season season, int time) {
            this.season = season;
            this.time = time;
        }

        @Override
        public String getDescription() {
            return "Set sunrise for the season " + season.getDisplayName() + " to " + time;
        }

        @Override
        public void apply() {
            original = CalendarAPI.INSTANCE.getDataForSeason(season).setSunrise(time);
        }

        @Override
        public void undo() {
            CalendarAPI.INSTANCE.getDataForSeason(season).setSunrise(original);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setSunset(String season, int time) {
        Season theSeason = getSeasonFromString(season);
        if (theSeason == null) CraftTweaker.logError("No season named " + season + " could be found");
        else MineTweakerAPI.apply(new SetSunset(theSeason, time));
    }

    private static class SetSunset extends BaseUndoable {
        private final Season season;
        private final int time;
        private int original;

        SetSunset(Season season, int time) {
            this.season = season;
            this.time = time;
        }

        @Override
        public String getDescription() {
            return "Set sunset for the season " + season.getDisplayName() + " to " + time;
        }

        @Override
        public void apply() {
            original = CalendarAPI.INSTANCE.getDataForSeason(season).setSunset(time);
        }

        @Override
        public void undo() {
            CalendarAPI.INSTANCE.getDataForSeason(season).setSunset(original);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setGrassColor(String season, String hex) {
        Season theSeason = getSeasonFromString(season);
        if (theSeason == null) CraftTweaker.logError("No season named " + season + " could be found");
        try {
            int number = Integer.parseInt(hex, 16);
            MineTweakerAPI.apply(new SetGrassColor(theSeason, number));
        } catch (NumberFormatException ex) { CraftTweaker.logError("Was not a valid hex colour"); }
    }

    private static class SetGrassColor extends BaseUndoable {
        private final Season season;
        private final int color;
        private int original;

        SetGrassColor(Season season, int color) {
            this.season = season;
            this.color = color;
        }

        @Override
        public String getDescription() {
            return "Set grass colour for the season " + season.getDisplayName() + " to " + color;
        }

        @Override
        public void apply() {
            original = CalendarAPI.INSTANCE.getDataForSeason(season).grassColor;
            CalendarAPI.INSTANCE.getDataForSeason(season).grassColor = color;
        }

        @Override
        public void undo() {
            CalendarAPI.INSTANCE.getDataForSeason(season).grassColor = original;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @ZenMethod
    @SuppressWarnings("unused")
    public static void setLeavesColor(String season, String hex) {
        Season theSeason = getSeasonFromString(season);
        if (theSeason == null) CraftTweaker.logError("No season named " + season + " could be found");
        try {
            int number = Integer.parseInt(hex, 16);
            MineTweakerAPI.apply(new SetLeavesColor(theSeason, number));
        } catch (NumberFormatException ex) { CraftTweaker.logError("Was not a valid hex colour"); }
    }

    private static class SetLeavesColor extends BaseUndoable {
        private final Season season;
        private final int color;
        private int original;

        SetLeavesColor(Season season, int color) {
            this.season = season;
            this.color = color;
        }

        @Override
        public String getDescription() {
            return "Set leaves colour for the season " + season.getDisplayName() + " to " + color;
        }

        @Override
        public void apply() {
            original = CalendarAPI.INSTANCE.getDataForSeason(season).leavesColor;
            CalendarAPI.INSTANCE.getDataForSeason(season).leavesColor = color;
        }

        @Override
        public void undo() {
            CalendarAPI.INSTANCE.getDataForSeason(season).leavesColor = original;
        }
    }
}
