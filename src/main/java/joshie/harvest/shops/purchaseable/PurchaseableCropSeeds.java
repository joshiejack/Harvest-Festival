package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.SeasonData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.minecraft.util.text.TextFormatting.WHITE;

public class PurchaseableCropSeeds implements IPurchaseable {
    private ItemStack product;
    private ICrop crop;

    public PurchaseableCropSeeds(ICrop crop) {
        this.crop = crop;
        this.product = crop.getSeedStack();
    }

    private boolean isCorrectSeason(Season theSeason) {
        for (Season season : crop.getSeasons()) {
            if (season == theSeason) return true;
        }

        return false;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        CalendarDate playersBirthday = HFTrackers.getPlayerTracker(player).getStats().getBirthday();
        CalendarDate date = HFApi.calendar.getDate(world);
        if (!isCorrectSeason(date.getSeason())) return false;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltip(List<String> list) {
        list.add(WHITE + crop.getSeedsName());
        for (Season season : crop.getSeasons()) {
            SeasonData data = CalendarAPI.INSTANCE.getDataForSeason(season);
            list.add(data.getTextColor() + data.getLocalized());
        }
    }

    @Override
    public boolean onPurchased(EntityPlayer player) {
        ItemHelper.addToPlayerInventory(player, product.copy());

        return false;
    }
}
