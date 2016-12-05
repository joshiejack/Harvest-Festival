package joshie.harvest.shops;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.IShop;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.npc.entity.EntityNPC;
import joshie.harvest.shops.gui.ShopSelection;
import joshie.harvest.shops.purchasable.Purchasable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

public class Shop implements IShop {
    private static final ISpecialRules DEFAULT_TRUE = (w, p, a) -> true;
    private final LinkedHashMap<String, IPurchasable> contents = new LinkedHashMap<>();
    private final HashMultimap<Weekday, OpeningHours> open = HashMultimap.create();
    public final ResourceLocation resourceLocation;
    private final String unlocalizedName;
    private ISpecialRules rulesBuying;
    private ISpecialRules rulesSelling;
    private ShopSelection selection;
    private boolean canBuy;
    private boolean canSell;

    public Shop(ResourceLocation resource) {
        resourceLocation = resource;
        unlocalizedName = resource.getResourceDomain() + ".shop." + resource.getResourcePath();
        rulesBuying = DEFAULT_TRUE;
        rulesSelling = DEFAULT_TRUE;
    }

    public IPurchasable getPurchasableFromID(String id) {
        return contents.get(id);
    }

    @Override
    public Shop setSpecialPurchaseRules(ISpecialRules rules) {
        this.rulesBuying = rules;
        return this;
    }

    @Override
    public Shop setSpecialSellingRules(ISpecialRules rules) {
        this.rulesSelling = rules;
        return this;
    }

    public boolean canBuyFromShop(@Nullable EntityPlayer player) {
        return canBuy && (player == null || rulesBuying.canDo(player.worldObj, player, 1));
    }

    public boolean canSellToShop(@Nullable EntityPlayer player) {
        return canSell && (player == null || rulesSelling.canDo(player.worldObj, player, 1));
    }

    public ShopSelection getSelection() {
        if (selection == null) {
            selection = new ShopSelection(this); //Default selection
        }

        return selection;
    }

    @Override
    public IShop addConditionalOpening(ISpecialRules rules, Weekday day, int opening, int closing) {
        open.get(day).add(new OpeningHours(rules, opening, closing));
        return this;
    }

    public void removeItem(IPurchasable item) {
        if (item != null) contents.remove(item.getPurchaseableID());
    }

    @Override
    public IShop addItem(IPurchasable item) {
        if (item != null) {
            if (!canBuy && item.getCost() >= 0) canBuy = true;
            if (!canSell && item.getCost() < 0) canSell = true;
            contents.put(item.getPurchaseableID(), item);
        }

        return this;
    }

    @Override
    public IShop addItem(long cost, ItemStack... items) {
        return addItem(new Purchasable(cost, items[0]));
    }

    @Override
    public IPurchasable addPurchasable(IPurchasable item) {
        if (item != null) {
            if (!canBuy && item.getCost() >= 0) canBuy = true;
            if (!canSell && item.getCost() < 0) canSell = true;
            contents.put(item.getPurchaseableID(), item);
        }

        return item;
    }

    @Override
    public IPurchasable addPurchasable(long cost, ItemStack stack) {
        return addPurchasable(new Purchasable(cost, stack));
    }

    /** My own convenience **/
    public IPurchasable addPurchasable(long cost, ItemStack stack, int stock) {
        return addPurchasable(new Purchasable(cost, stack).setStock(stock));
    }

    public String getLocalizedName() {
        return TextHelper.localize(unlocalizedName);
    }

    public String getWelcome(EntityNPC npc) {
        return TextHelper.getRandomSpeech(npc.getNPC(), unlocalizedName + ".greeting", 100);
    }

    public Set<String> getPurchasableIDs() {
        return contents.keySet();
    }

    public Collection<IPurchasable> getContents() {
        return contents.values();
    }

    @SuppressWarnings("unchecked")
    public boolean isOpen(World world, EntityNPC npc, @Nullable EntityPlayer player) {
        if (HFShops.TWENTY_FOUR_HOUR_SHOPPING) return true;
        Weekday day = HFApi.calendar.getDate(world).getWeekday();
        for (OpeningHours hours: open.get(day)) {
            boolean isOpen = CalendarHelper.isBetween(world, hours.open, hours.close) &&
                    (hours.rules == null || hours.rules.canDo(world, npc, 0));
            if (isOpen && (player == null || contents.size() > 0)) return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean isPreparingToOpen(World world, EntityNPC npc) {
        if (HFShops.TWENTY_FOUR_HOUR_SHOPPING) return false;
        Weekday day = HFApi.calendar.getDate(world).getWeekday();
        for (OpeningHours hours: open.get(day)) {
            long daytime = CalendarHelper.getTime(world); //0-23999 by default
            int hourHalfBeforeWork = fix(CalendarHelper.getScaledTime(hours.open) - 1500);
            if(daytime >= hourHalfBeforeWork && (hours.rules == null || hours.rules.canDo(world, npc, 0))) return true;
        }

        return false;
    }

    private int fix(int i) {
        return Math.min(24000, Math.max(0, i));
    }

    /** The integers in here are as follows
     * 1000 = 1 AM
     * 2500 = 2:30am
     * 18000 = 6PM
     * etc. */
    private static class OpeningHours {
        private final ISpecialRules rules;
        private final int open;
        private final int close;

        OpeningHours(ISpecialRules rules, int open, int close) {
            this.rules = rules;
            this.open = open;
            this.close = close;
        }
    }
}