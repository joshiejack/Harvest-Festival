package joshie.harvest.plugins.harvestcraft;

import java.util.ArrayList;

import joshie.harvest.api.HFApi;
import joshie.harvest.asm.overrides.ItemPamSeedFood;
import joshie.harvest.calendar.Season;
import joshie.harvest.plugins.HFPlugins.Plugin;

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
        crops.add((HarvestCraftCrop) HFApi.CROPS.registerCrop(new HarvestCraftCrop(unlocalized, stages, regrow, color, seasons)));
    }
}
