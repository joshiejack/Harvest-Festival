package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.cooking.HFCooking;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PurchasableRecipe extends PurchasableMeal {
    private final Season season;
    private final Weekday weekday;

    public PurchasableRecipe(Season season, Weekday weekday, ResourceLocation resource) {
        super(150, resource);
        this.season = season;
        this.weekday = weekday;
        this.setStock(1);
    }

    @Override
    public boolean canBuy(World world, EntityPlayer player, int amount) {
        CalendarDate date = HFApi.calendar.getDate(world);
        return amount == 1 && date.getWeekday() == weekday && date.getSeason() == season;
    }

    @Override
    public ItemStack getDisplayStack() {
        return HFCooking.RECIPE.getStackFromObject(item);
    }
}
