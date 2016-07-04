package joshie.harvest.api.calendar;

public enum Weather {
    SUNNY, RAIN, TYPHOON, SNOW, BLIZZARD;
    
    public boolean isRain() {
        return this == RAIN || this == TYPHOON;
    }
       
    public boolean isSnow() {
        return this == SNOW || this == BLIZZARD;
    }
    
    public boolean isBadWeather() {
        return this == TYPHOON || this == BLIZZARD;
    }

    public boolean isUndesirable() {
        return isBadWeather() || isRain();
    }

    public boolean isSunny() {
        return this == SUNNY;
    }
}