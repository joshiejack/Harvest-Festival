package joshie.harvest.shops;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.core.ISpecialPurchaseRules;
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

public class Shop implements IShop {
    public static final ISpecialPurchaseRules DEFAULT = (w, p, a) -> true;
    private final LinkedHashMap<String, IPurchasable> contents = new LinkedHashMap<>();
    private final HashMultimap<Weekday, OpeningHours> open = HashMultimap.create();
    public final ResourceLocation resourceLocation;
    private final String unlocalizedName;
    private ISpecialPurchaseRules rules;
    private ShopSelection selection;
    private boolean canBuy;
    private boolean canSell;
    private long budget;

    public Shop(ResourceLocation resource) {
        budget = 5000L;
        resourceLocation = resource;
        unlocalizedName = resource.getResourceDomain() + ".shop." + resource.getResourcePath();
        rules = DEFAULT;
    }

    public long getBudget() {
        return budget;
    }

    public IPurchasable getPurchasableFromID(String id) {
        return contents.get(id);
    }

    @Override
    public Shop setSpecialPurchaseRules(ISpecialPurchaseRules rules) {
        this.rules = rules;
        return this;
    }

    public boolean canBuyFromShop(@Nullable EntityPlayer player) {
        return player == null ? canBuy : rules.canBuy(player.worldObj, player, 1);
    }

    public boolean canSellToShop(@Nullable EntityPlayer player) {
        return player == null ? canSell : rules.canBuy(player.worldObj, player, 1);
    }

    public ShopSelection getSelection() {
        if (selection == null) {
            selection = new ShopSelection(this); //Default selection
        }

        return selection;
    }

    @Override
    public IShop addOpening(Weekday day, int opening, int closing) {
        open.get(day).add(new OpeningHours(opening, closing));
        return this;
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

    public static String getRegistryName(ResourceLocation resource, IPurchasable item) {
        return resource + ":" + item.getPurchaseableID();
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

    public Collection<IPurchasable> getContents() {
        return contents.values();
    }

    public boolean isOpen(World world, @Nullable EntityPlayer player) {
        if (HFShops.TWENTY_FOUR_HOUR_SHOPPING) return true;
        Weekday day = HFApi.calendar.getDate(world).getWeekday();
        for (OpeningHours hours: open.get(day)) {
            boolean isOpen = CalendarHelper.isBetween(world, hours.open, hours.close);
            if (isOpen && (player == null || contents.size() > 0)) return true;
        }

        return false;
    }

    public boolean isPreparingToOpen(World world) {
        if (HFShops.TWENTY_FOUR_HOUR_SHOPPING) return false;
        Weekday day = HFApi.calendar.getDate(world).getWeekday();
        for (OpeningHours hours: open.get(day)) {
            long daytime = CalendarHelper.getTime(world); //0-23999 by default
            int hourHalfBeforeWork = fix(CalendarHelper.getScaledTime(hours.open) - 1500);
            if(daytime >= hourHalfBeforeWork) return true;
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
        private final int open;
        private final int close;

        OpeningHours(int open, int close) {
            this.open = open;
            this.close = close;
        }
    }
}