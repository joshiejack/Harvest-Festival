package joshie.harvestmoon.shops;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.helpers.CalendarHelper;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableCropSeeds implements IPurchaseable {
    private Crop crop;

    public PurchaseableCropSeeds(Crop crop) {
        this.crop = crop;
    }

    @Override
    public boolean canBuy(World world, CalendarDate playersBirthday, CalendarDate date) {
        if (date.getSeason() != crop.getSeason()) return false;
        if (!crop.canPurchase()) return false;       
        if (CalendarHelper.getYearsPassed(playersBirthday, date) >= crop.getPurchaseYear()) {
            return date.getSeason() == crop.getSeason();
        } else return false;
    }

    @Override
    public long getCost() {
        return crop.getSeedCost();
    }

    @Override
    public ItemStack getProduct() {
        return new ItemStack(HMItems.seeds, 1, crop.getCropMeta());
    }
}
