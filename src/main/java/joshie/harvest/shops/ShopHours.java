package joshie.harvest.shops;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Festival;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.shops.OpeningHandler;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.calendar.CalendarHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ShopHours implements OpeningHandler {
    private final HashMultimap<Weekday, OpeningHours> open = HashMultimap.create();
    private boolean opensOnHolidays;

    public ShopHours setOpensOnHolidays() {
        this.opensOnHolidays = true;
        return this;
    }

    @Override
    public void addOpening(Weekday day, int opening, int closing, ISpecialRules... rules) {
        ISpecialRules theRules = rules.length == 1 ? rules[0] : null;
        open.get(day).add(new OpeningHours(theRules, opening, closing));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isOpen(World world, EntityAgeable npc, @Nullable EntityPlayer player, Shop shop) {
        Festival festival = HFApi.calendar.getFestival(world, new BlockPos(npc));
        if (!opensOnHolidays && !festival.doShopsOpen() && festival != Festival.NONE) return false;
        Weekday day = HFApi.calendar.getDate(world).getWeekday();
        for (OpeningHours hours: open.get(day)) {
            boolean isOpen = CalendarHelper.isBetween(world, hours.open, hours.close) && (player == null || hours.rules == null || hours.rules.canDo(world, player, 0));
            if (isOpen && (shop.getPurchasableIDs().size() > 0)) return true;
        }

        return false;
    }

    /** The integers in here are as follows
     * 1000 = 1 AM
     * 2500 = 2:30am
     * 18000 = 6PM
     * etc. */
    public static class OpeningHours {
        public final ISpecialRules rules;
        public final int open;
        public final int close;

        OpeningHours(ISpecialRules rules, int open, int close) {
            this.rules = rules;
            this.open = open;
            this.close = close;
        }
    }
}
