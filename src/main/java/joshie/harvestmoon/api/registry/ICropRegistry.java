package joshie.harvestmoon.api.registry;

import joshie.harvestmoon.api.Calendar.ISeason;
import joshie.harvestmoon.api.crops.ICrop;

public interface ICropRegistry {
    public ICrop registerCrop(String unlocalized, ISeason season, int cost, int sell, int stages, int regrow, int year, int color);

    public ICrop registerCrop(ICrop crop);

    public ICrop getCrop(String unlocalized);
}
