package joshie.harvestmoon.shops;

import java.util.List;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.shops.IPurchaseable;
import joshie.harvestmoon.calendar.CalendarDate;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.helpers.PlayerHelper;
import joshie.harvestmoon.core.helpers.generic.ItemHelper;
import joshie.harvestmoon.core.util.generic.Text;
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
    public ItemStack getDisplayStack() {
        return product;
    }
    
    @Override
    public void addTooltip(List list) {
        list.add(Text.WHITE + crop.getSeedsName());
        for (Season season : crop.getSeasons()) {
            list.add(season.getTextColor() + season.getLocalized());
        }
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        ItemHelper.addToPlayerInventory(player, product.copy());
        
        return false;
    }
}
