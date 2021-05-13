package uk.joshiejack.economy.shop.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.economy.network.PacketSetStockedItem;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.MapHelper;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;

import java.util.Random;

public class Stock implements INBTSerializable<NBTTagCompound> {
    private static final Random initialRandom = new Random();
    private final Object2IntMap<Listing> stockLevels = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Listing> stockItems = new Object2IntOpenHashMap<>();
    private final Department shop;

    public Stock(Department shop) {
        this.shop = shop;
        for (Listing purchasable: shop.getListings()) {
            if (!purchasable.isSingleEntry() && !stockItems.containsKey(purchasable)) {
                stockItems.put(purchasable, purchasable.getRandomID(initialRandom));
            }
        }
    }

    public void decreaseStockLevel(Listing purchasable) {
        MapHelper.adjustValue(stockLevels, purchasable, -1);
    }

    public void setStockedItem(Listing listing, int stockID) {
        stockItems.put(listing, stockID);
    }

    public int getStockedObject(Listing purchasable) {
        if (!stockItems.containsKey(purchasable)) {
            stockItems.put(purchasable, purchasable.getRandomID(initialRandom));
            return 0;
        } else return stockItems.getInt(purchasable);
    }

    public int getStockLevel(Listing purchasable){
        return MapHelper.adjustOrPut(stockLevels, purchasable, 0, purchasable.getStockMechanic().getMaximum());
    }

    public void newDay(Random random) {
        for (Listing purchasable: stockLevels.keySet()) {
            StockMechanic mechanic = purchasable.getStockMechanic();
            stockLevels.put(purchasable, MathsHelper.constrainToRangeInt(getStockLevel(purchasable) + mechanic.getIncrease(), 0, mechanic.getMaximum()));
        }

        for (Listing purchasable: stockItems.keySet()) {
            int id = purchasable.getRandomID(random);
            stockItems.put(purchasable, id);
            PenguinNetwork.sendToEveryone(new PacketSetStockedItem(shop, purchasable, id));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound data = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        stockLevels.forEach((key, value) -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Key", key.getID());
            tag.setInteger("Value", value);
            list.appendTag(tag);
        });

        data.setTag("Levels", list);

        NBTTagList stockList = new NBTTagList();
        stockItems.forEach((key, value) -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Key", key.getID());
            tag.setInteger("Value", value);
            stockList.appendTag(tag);
        });

        data.setTag("Stock", stockList);

        return data;
    }

    @Override
    public void deserializeNBT(NBTTagCompound data) {
        NBTTagList nbt = data.getTagList("Levels", 10);
        for (int i = 0; i < nbt.tagCount(); i++) {
            NBTTagCompound tag = nbt.getCompoundTagAt(i);
            Listing purchasable = shop.getListingByID(tag.getString("Key"));
            if (purchasable != null) {
                stockLevels.put(purchasable, tag.getInteger("Value"));
            }
        }

        NBTTagList stock = data.getTagList("Stock", 10);
        for (int i = 0; i < stock.tagCount(); i++) {
            NBTTagCompound tag = stock.getCompoundTagAt(i);
            Listing purchasable = shop.getListingByID(tag.getString("Key"));
            if (purchasable != null) {
                stockItems.put(purchasable, tag.getInteger("Value"));
            }
        }
    }
}
