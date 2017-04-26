package joshie.harvest.shops.purchasable;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.knowledge.HFNotes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PurchasableRecipe extends PurchasableMeal {
    private final Season season;
    private final Weekday weekday;

    public PurchasableRecipe(ResourceLocation resource) {
        this(resource, 300);
    }

    public PurchasableRecipe(ResourceLocation resource, long cost) {
        super(cost, resource);
        this.setStock(1);
        this.setNote(HFNotes.RECIPES);
        this.season = null;
        this.weekday = null;
    }

    public PurchasableRecipe(Season season, Weekday weekday, ResourceLocation resource) {
        super(150, resource);
        this.season = season;
        this.weekday = weekday;
        this.setStock(1);
        this.setNote(HFNotes.RECIPES);
    }

    @Override
    public boolean canDo(@Nonnull World world, @Nonnull EntityPlayer player, int amount) {
        if (season == null || weekday == null) return amount == 1;
        CalendarDate date = HFApi.calendar.getDate(world);
        return amount == 1 && date.getWeekday() == weekday && date.getSeason() == season;
    }

    @Override
    @Nonnull
    public ItemStack getDisplayStack() {
        return HFCooking.RECIPE.getStackFromObject(recipe);
    }
}
