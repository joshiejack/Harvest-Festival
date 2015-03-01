package joshie.harvestmoon.plugins.harvestcraft;

import java.util.ArrayList;

import joshie.harvestmoon.api.HMApi;
import joshie.harvestmoon.asm.overrides.ItemPamSeedFood;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.plugins.HMPlugins.Plugin;

public class HarvestCraft extends Plugin {
    public static ArrayList<HarvestCraftCrop> crops = new ArrayList();

    @Override
    public void preInit() {
        addCrop("beanItem", 7, 1, 0xB00000, Season.SUMMER);
    }

    @Override
    public void init() {
        for (HarvestCraftCrop crop : crops) {
            crop.loadItem();
        }
    }

    @Override
    public void postInit() {
        ItemPamSeedFood.isLoaded = true;
        crops = null; //Clear out the cache
    }

    private void addCrop(String unlocalized, int stages, int regrow, int color, Season... seasons) {
        crops.add((HarvestCraftCrop) HMApi.CROPS.registerCrop(new HarvestCraftCrop(unlocalized, stages, regrow, color, seasons)));
    }
}
