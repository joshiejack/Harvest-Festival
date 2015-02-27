package joshie.harvestmoon.plugins.harvestcraft;

import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.Crop;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.IPlantable;
import cpw.mods.fml.common.registry.GameRegistry;

public class HarvestCraftCrop extends Crop {
    private String itemName;
    private String seedItemName;
    private String seedName;

    public HarvestCraftCrop(String unlocalized, int stages, int regrow, int color, Season... seasons) {
        super(unlocalized, seasons, 0, 0, stages, regrow, 0, color);
        this.itemName = unlocalized;
        this.seedItemName = unlocalized.replace("Item", "seedItem");
        this.unlocalized = "item." + unlocalized + ".name";
        this.seedName = this.unlocalized.replace("Item.name", "seedItem.name");
    }

    @Override
    public String getLocalizedName(boolean isItem) {
        return StatCollector.translateToLocal(getUnlocalizedName());
    }

    @Override
    public String getSeedsName() {
        return StatCollector.translateToLocal(seedName);
    }

    @Override
    public boolean hasItemAssigned() {
        return true;
    }

    //Attempt to load the harvestcraft item
    public void loadItem() {
        setItem(GameRegistry.findItem("harvestcraft", itemName));
        Item seeds = GameRegistry.findItem("harvestcraft", seedItemName);
        if (seeds instanceof IPlantable) {
            setCropIconHandler(new IconHandlerHarvestCraft(((IPlantable) seeds).getPlant(null, 0, 0, 0), getStages()));
        }

        seeds = null;
    }
}
