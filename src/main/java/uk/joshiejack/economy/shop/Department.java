package uk.joshiejack.economy.shop;

import com.google.common.collect.Maps;
import joptsimple.internal.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.inventory.ContainerShop;
import uk.joshiejack.economy.network.PacketOpenShop;
import uk.joshiejack.economy.network.PacketSyncStockLevels;
import uk.joshiejack.economy.shop.input.InputMethod;
import uk.joshiejack.economy.shop.inventory.Inventory;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetPlayerStatus;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public class Department {
    public static final Map<String, Department> REGISTRY = Maps.newHashMap();
    private final Map<String, Listing> listings = Maps.newLinkedHashMap();
    private final NonNullList<Condition> conditions = NonNullList.create();
    private final String id;
    private final Shop shop;
    private ItemStack icon;
    private String name;
    private final InputMethod method;
    private String outof;

    public Department(Shop shop, String department_id, InputMethod method) {
        this.id = department_id;
        this.shop = shop;
        this.name = Strings.EMPTY;
        this.outof = Economy.MODID + ".shop.outof";
        this.icon = ItemStack.EMPTY;
        this.method = method;
        this.shop.getDepartments().add(this);
        Department.REGISTRY.put(department_id, this);
    }

    @Nullable
    public Shop getShop() {
        return shop;
    }

    public String id() {
        return id;
    }

    public Department setName(String name) {
        if (name.contains(":")) {
            String[] split = ResourceLocation.splitObjectName(name);
            this.name = split[0] + ".shop.department." + split[1] + ".name";
            this.outof = split[0] + ".shop.department." + split[1] + ".outof";
        } else this.name = name;

        return this;
    }

    public Department setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }

    public void addListing(Listing listing) {
        listings.put(listing.getID(), listing);
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public Listing getListingByID(String id) {
        return listings.get(id);
    }

    public Collection<Listing> getListings() {
        return listings.values();
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getLocalizedName() {
        return StringHelper.localize(name);
    }

    public String getOutofText() {
        String translated = StringHelper.localize(outof);
        return outof.equals(translated) ? StringHelper.localize(Economy.MODID + ".shop.outof") : translated;
    }

    public boolean isValidFor(ShopTarget target, Condition.CheckType type, InputMethod method) {
        return this.method == method && isValidFor(target, type);
    }

    public boolean isValidFor(ShopTarget target, Condition.CheckType type) {
        return conditions.stream().allMatch(condition -> condition.valid(target, type));
    }

    public void open(ShopTarget target) {
        //Sync supermarket inventories too
        Shop market = Shop.get(this);
        if (market != null) {
            market.getDepartments().forEach(shop -> PenguinNetwork.sendToClient(new PacketSyncStockLevels(shop, Inventory.get(target.world).getStockForDepartment(shop)), target.player));
        } else
            PenguinNetwork.sendToClient(new PacketSyncStockLevels(this, Inventory.get(target.world).getStockForDepartment(this)), target.player); //Sync the stock levels to the player
        /* Seed the random shopness */
        int seed = 13 * (1 + TimeHelper.getElapsedDays(target.world.getWorldTime()));
        target.player.getEntityData().getCompoundTag("PenguinStatuses").setInteger("ShopSeed", seed);
        PenguinNetwork.sendToClient(new PacketSetPlayerStatus("ShopSeed", seed), target.player);
        /* Open the shop */ //Open after the stock level has been received
        PenguinNetwork.sendToClient(new PacketOpenShop(this, target), target.player);
        target.player.openContainer = new ContainerShop(); //Set the container
    }
}
