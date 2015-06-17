package joshie.harvest.shops.purchaseable;

import java.util.List;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.generic.ItemHelper;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PurchaseableCropSeeds implements IPurchaseable {
    private ItemStack product;
    private ICrop crop;

    public PurchaseableCropSeeds(ICrop crop) {
        this.crop = crop;
        this.product = crop.getSeedStack();
    }

    private boolean isCorrectSeason(ICalendarDate date) {
        for (Season season : crop.getSeasons()) {
            if (season == date.getSeason()) return true;
        }

        return false;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        ICalendarDate playersBirthday = HFTrackers.getPlayerTracker(player).getStats().getBirthday();
        ICalendarDate date = HFTrackers.getCalendar().getDate();
        if (!isCorrectSeason(date)) return false;
        if (!crop.canPurchase()) return false;
        if (CalendarHelper.getYearsPassed(playersBirthday, date) >= crop.getPurchaseYear()) {
            return true;
        } else return false;
    }

    @Override
    public boolean canList(World world, EntityPlayer player) {
        return canBuy(world, player);
    }

    @Override
    public long getCost() {
        return crop.getSeedCost();
    }

    @Override
    public ItemStack getDisplayStack() {
        return product;
    }

    @Override
    public void addTooltip(List list) {
        list.add(Text.WHITE + crop.getSeedsName());
        for (Season season : crop.getSeasons()) {
            ISeasonData data = HFApi.CALENDAR.getDataForSeason(season);
            list.add(data.getTextColor() + data.getLocalized());
        }
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        ItemHelper.addToPlayerInventory(player, product.copy());

        return false;
    }
}
