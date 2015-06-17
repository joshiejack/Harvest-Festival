package joshie.harvest.api.calendar;

public enum Weather {
    SUNNY, DRIZZLE, RAIN, TYPHOON, SNOW, BLIZZARD;
       
    public boolean isSnow() {
        return this == SNOW || this == BLIZZARD;
    }
}
