package joshie.harvest.shops.purchaseable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.cooking.CookingHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PurchaseableRecipe extends Purchaseable {
    private final Season season;
    private final Weekday weekday;

    public PurchaseableRecipe(Season season, Weekday weekday, String recipe) {
        super(150, CookingHelper.getRecipe(recipe));
        this.season = season;
        this.weekday = weekday;
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player) {
        CalendarDate date = HFApi.calendar.getDate(world);
        return date.getWeekday() == weekday && date.getSeason() == season;
    }
}
