package joshie.harvest.shops.data;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.helpers.CollectionHelper;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.util.interfaces.INBTSerializableMap;
import joshie.harvest.player.tracking.StackSold;
import joshie.harvest.shops.Shop;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShopInventory implements INBTSerializableMap<Shop, ShopInventory, NBTTagCompound> {
    private Set<StackSold> soldToShop = new HashSet<>();
    private Set<StackSold> purchasedFromShop = new HashSet<>();
    private Shop shop;

    public ShopInventory() {}
    public ShopInventory(Shop shop) {
        this.shop = shop;
    }

    public void onItemSoldToShop(IPurchasable purchasable) {
        StackSold stack = StackSold.of(purchasable.getDisplayStack(), purchasable.getCost());
        CollectionHelper.mergeCollection(stack, soldToShop);
    }

    public StackSold onItemPurchasedFromShop(IPurchasable purchasable) {
        StackSold stack = StackSold.of(purchasable.getDisplayStack(), purchasable.getCost());
        return CollectionHelper.mergeCollection(stack, purchasedFromShop);
    }

    public long getAdjustedSellToShopValue(IPurchasable purchasable) {
        return Math.min(0, (long)(purchasable.getCost() * (1 - (double)getAmountPurchased(purchasable)/purchasable.getStock())));
    }

    public boolean canList(IPurchasable purchasable) {
        return purchasable.getCost() < 0 ? getAdjustedSellToShopValue(purchasable) < 0 : getAmountPurchased(purchasable) < purchasable.getStock();
    }

    private int getAmountPurchased(IPurchasable purchasable) {
        StackSold comparable = StackSold.of(purchasable.getDisplayStack(), purchasable.getCost());
        for (StackSold sold: (purchasable.getCost() < 0) ? soldToShop : purchasedFromShop) {
            if (sold.equals(comparable)) return sold.getAmount();

        }

        return 0;
    }

    public void newDay() {
        purchasedFromShop.stream().forEach(StackSold::reset);
        soldToShop.stream().forEach(StackSold::depreciate);
    }

    @Override
    public void buildMap(Map<Shop, ShopInventory> map) {
        map.put(shop, this);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        shop = (Shop) HFApi.shops.getShop(new ResourceLocation(nbt.getString("Shop")));
        soldToShop = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("Purchased", 10));
        purchasedFromShop = NBTHelper.readHashSet(StackSold.class, nbt.getTagList("Sold", 10));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("Shop", shop.resourceLocation.toString());
        tag.setTag("Purchased", NBTHelper.writeCollection(soldToShop));
        tag.setTag("Sold", NBTHelper.writeCollection(purchasedFromShop));
        return tag;
    }
}
