package joshie.harvestmoon.shops;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.shops.IPurchaseable;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.helpers.PlayerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableCropSeeds implements IPurchaseable {
    private ICrop crop;

    public PurchaseableCropSeeds(ICrop crop) {
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
        return new ItemStack[] { crop.getSeedStack() };
    }
}
