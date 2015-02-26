package joshie.harvestmoon.api.registry;

import joshie.harvestmoon.api.Calendar.ISeason;
import joshie.harvestmoon.api.interfaces.ICrop;

public interface ICropRegistry {
    public ICrop registerCrop(String unlocalized, ISeason season, int cost, int sell, int stages, int regrow, int year, int color);

    public ICrop registerCrop(String unlocalized, ISeason[] seasons, int cost, int sell, int stages, int regrow, int year, int color);
}
