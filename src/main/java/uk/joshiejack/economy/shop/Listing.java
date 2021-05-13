package uk.joshiejack.economy.shop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.event.ItemPurchasedEvent;
import uk.joshiejack.economy.shop.inventory.Inventory;
import uk.joshiejack.economy.shop.inventory.Stock;
import uk.joshiejack.economy.shop.inventory.StockMechanic;
import uk.joshiejack.penguinlib.scripting.Interpreter;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.loot.LootRegistryWithID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Listing {
    public static final Map<String, ListingHandler> HANDLERS = Maps.newHashMap();
    protected final List<Condition> conditions = Lists.newArrayList();
    private final LootRegistryWithID<Sublisting> sublistings = new LootRegistryWithID<>();
    private final String listing_id;
    protected final Department department;
    private final StockMechanic stockMechanic;
    private final Interpreter costScript;
    private static final Random random = new Random();

    public Listing(Department department, String listing_id, List<Sublisting> sublistings, Interpreter costScript, StockMechanic stockMechanic) {
        this.department = department;
        this.listing_id = listing_id;
        this.costScript = costScript;
        this.stockMechanic = stockMechanic;
        this.department.addListing(this);
        for (int id = 0; id < sublistings.size(); id++) {
            this.sublistings.add(id, sublistings.get(id).setIntID(id), sublistings.get(id).getWeight());
        }
    }

    public LootRegistryWithID<Sublisting> getSublistings() { return sublistings; }

    public boolean isSingleEntry() {
        return sublistings.isSingleEntry();
    }

    public Sublisting getSublistingByID(int id) {
        return sublistings.byID(id);
    }

    public int getRandomID(Random random) {
        return sublistings.get(random).int_id();
    }

    public StockMechanic getStockMechanic() {
        return stockMechanic;
    }

    public String getID() {
        return listing_id;
    }

    public long getGoldCost(EntityPlayer player, Stock stock) {
        if (costScript == null) {
            Economy.logger.log(Level.ERROR, "Cost script was null for: " + department.id() + ":" + listing_id);
            return 0L;
        } else {
            random.setSeed(player.getEntityData().getCompoundTag("PenguinStatuses").getInteger("ShopSeed") + listing_id.hashCode() * 3643257684289L); //Get the shop seed
            long result = Scripting.getResult(costScript, "getCost", 999999999L, getSubListing(stock), stock.getStockLevel(this), stockMechanic, random);
            if (result == 999999999L) {
                Economy.logger.log(Level.ERROR, "Had an error while processing getCost for the item: " + department.id() + ":" + listing_id);
                return 999999999;
            } else return result;
        }
    }

    public Sublisting getSubListing(Stock stock) {
        return isSingleEntry() ? getSublistingByID(0) : getSublistingByID(stock.getStockedObject(this));
    }

    /**
     * If this can be listed for this player
     **/
    public boolean canList(@Nonnull ShopTarget target, Stock stock) {
        for (Condition condition : conditions) {
            if (!condition.valid(target, department, this, Condition.CheckType.SHOP_LISTING)) {
                return false;
            }
        }

        return stock.getStockLevel(this) > 0;
    }

    /**
     * If the player is able to purchase this
     * Gold has already been checked
     **/
    public boolean canPurchase(EntityPlayer player, Stock stock, int amount) {
        return getSubListing(stock).hasMaterialRequirement(player, amount) && stock.getStockLevel(this) >= amount;
    }

    public void purchase(EntityPlayer player) {
        Inventory inventory = Inventory.get(player.world);
        Stock stock = inventory.getStockForDepartment(department);
        stock.decreaseStockLevel(this);
        getSubListing(stock).purchase(player);
        inventory.markDirty();
        conditions.forEach(c -> c.onPurchase(player, department, this));
        MinecraftForge.EVENT_BUS.post(new ItemPurchasedEvent(player, department, this));
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return Objects.equals(listing_id, listing.listing_id) && Objects.equals(department, listing.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listing_id, department);
    }

    public Department getDepartment() {
        return department;
    }
}
