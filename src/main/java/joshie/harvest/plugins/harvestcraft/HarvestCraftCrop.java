package joshie.harvest.plugins.harvestcraft;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.crops.Crop;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.IPlantable;

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
        return I18n.translateToLocal(getUnlocalizedName());
    }

    @Override
    public String getSeedsName() {
        return I18n.translateToLocal(seedName);
    }

    @Override
    public boolean hasItemAssigned() {
        return true;
    }

    //Attempt to load the harvestcraft item
    public void loadItem() {
        setItem(new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", itemName))));
        Item seeds = Item.REGISTRY.getObject(new ResourceLocation("harvestcraft", seedItemName));
        if (seeds instanceof IPlantable) {
            //setCropIconHandler(new IconHandlerHarvestCraft(((IPlantable) seeds).getPlant(null, 0, 0, 0), getStages()));
        }

        seeds = null;
    }
}