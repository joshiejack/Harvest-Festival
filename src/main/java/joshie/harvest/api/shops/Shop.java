package joshie.harvest.api.shops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Weekday;
import joshie.harvest.api.core.ISpecialRules;
import joshie.harvest.api.npc.NPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nullable;
import java.util.*;

public class Shop {
    public static final HashMap<ResourceLocation, Shop> REGISTRY = new HashMap<>();
    private static final ISpecialRules DEFAULT_TRUE = (w, p, a) -> true;
    private final LinkedHashMap<String, IPurchasable> contents = new LinkedHashMap<>();
    public final ResourceLocation resourceLocation;
    private final String unlocalizedName;
    private ISpecialRules rulesBuying;
    private ISpecialRules rulesSelling;
    private OpeningHandler openings;
    private boolean canBuy;
    private boolean canSell;

    /** Create a new shop with this id **/
    public Shop(ResourceLocation resource) {
        resourceLocation = resource;
        unlocalizedName = resource.getResourceDomain() + ".shop." + resource.getResourcePath();
        rulesBuying = DEFAULT_TRUE;
        rulesSelling = DEFAULT_TRUE;
        openings = HFApi.shops.createDefaultOpeningHandler();
        REGISTRY.put(resource, this);
    }

    public Shop setSpecialRules(ISpecialRules rules) {
        return setSpecialPurchaseRules(rules).setSpecialSellingRules(rules);
    }

    @SuppressWarnings("unused")
    public Shop setSpecialPurchaseRules(ISpecialRules rules) {
        this.rulesBuying = rules;
        return this;
    }

    public Shop setSpecialSellingRules(ISpecialRules rules) {
        this.rulesSelling = rules;
        return this;
    }

    public Shop setOpensOnHolidays() {
        getOpeningHandler().setOpensOnHolidays();
        return this;
    }

    public OpeningHandler getOpeningHandler() {
        return openings;
    }

    public IPurchasable getPurchasableFromID(String id) {
        return contents.get(id);
    }

    @SuppressWarnings("unchecked")
    public boolean canBuyFromShop(@Nullable EntityPlayer player) {
        return canBuy && (player == null || rulesBuying.canDo(player.worldObj, player, 1));
    }

    @SuppressWarnings("unchecked")
    public boolean canSellToShop(@Nullable EntityPlayer player) {
        return canSell && (player == null || rulesSelling.canDo(player.worldObj, player, 1));
    }

    public void removeItem(IPurchasable item) {
        if (item != null) contents.remove(item.getPurchaseableID());
    }

    public Shop addItem(IPurchasable item) {
        if (item != null) {
            if (!canBuy && item.getCost() >= 0) canBuy = true;
            if (!canSell && item.getCost() < 0) canSell = true;
            contents.put(item.getPurchaseableID(), item);
        }

        return this;
    }

    public Shop addConditionalOpening(ISpecialRules rules, Weekday day, int opening, int closing) {
        getOpeningHandler().addOpening(day, opening, closing, rules);
        return this;
    }

    public Shop addOpening(Weekday day, int opening, int closing) {
        getOpeningHandler().addOpening(day, opening, closing);
        return this;
    }

    public IPurchasable addPurchasable(IPurchasable item) {
        if (item != null) {
            if (!canBuy && item.getCost() >= 0) canBuy = true;
            if (!canSell && item.getCost() < 0) canSell = true;
            contents.put(item.getPurchaseableID(), item);
        }

        return item;
    }

    public IPurchasable addPurchasable(long cost, ItemStack stack) {
        return addPurchasable(HFApi.shops.createDefaultPurchasable(cost, stack));
    }

    public IPurchasable addPurchasable(long cost, ItemStack stack, int stock) {
        return addPurchasable(HFApi.shops.createDefaultPurchasableWithLimitedStock(cost, stack, stock));
    }

    public String getWelcome(NPC npc) {
        return HFApi.npc.getRandomSpeech(npc, unlocalizedName + ".greeting", 100);
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(unlocalizedName);
    }

    public Set<String> getPurchasableIDs() {
        return contents.keySet();
    }

    public Collection<IPurchasable> getContents() {
        return contents.values();
    }
}