package joshie.harvestmoon.shops;

import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.crops.Crop;
import joshie.harvestmoon.helpers.CalendarHelper;
import joshie.harvestmoon.helpers.PlayerHelper;
import joshie.harvestmoon.init.HMItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableCropSeeds implements IPurchaseable {
    private Crop crop;

    public PurchaseableCropSeeds(Crop crop) {
        this.crop = crop;
    }

    private boolean isCorrectSeason(CalendarDate date) {
        for (Season season : crop.getSeasons()) {
            if (season == date.getSeason()) return true;
        }

        return false;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        CalendarDate playersBirthday = PlayerHelper.getBirthday(player);
        CalendarDate date = CalendarHelper.getServerDate();
        if (!isCorrectSeason(date)) return false;
        if (!crop.canPurchase()) return false;
        if (CalendarHelper.getYearsPassed(playersBirthday, date) >= crop.getPurchaseYear()) {
            return true;
        } else return false;
    }

    @Override
    public long getCost() {
        return crop.getSeedCost();
    }

    @Override
    public ItemStack[] getProducts() {
        return new ItemStack[] { new ItemStack(HMItems.seeds, 1, crop.getCropMeta()) };
    }
}
