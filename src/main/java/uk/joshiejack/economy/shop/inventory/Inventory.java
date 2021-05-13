package uk.joshiejack.economy.shop.inventory;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import uk.joshiejack.economy.shop.Department;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Inventory extends WorldSavedData {
    private static final String DATA_NAME = "Shop-Stock";
    private static final Inventory CLIENT = new Inventory(DATA_NAME);
    private static Inventory instance;
    private final Map<Department, Stock> stockLevels = Maps.newHashMap();

    public Inventory(String data) {
        super(data);
    }

    public static Inventory get(World world) {
        if (world.isRemote) return CLIENT;
        else {
            instance = (Inventory) world.loadData(Inventory.class, DATA_NAME);
            if (instance == null) {
                instance = new Inventory(DATA_NAME);
                world.setData(DATA_NAME, instance);
                instance.markDirty(); //Save the file
            }

            return instance;
        }
    }

    public void newDay(Random random) {
        stockLevels.values().forEach(s -> s.newDay(random));
        markDirty();
    }

    public Stock getStockForDepartment(Department shop) {
        if (!stockLevels.containsKey(shop)) {
            stockLevels.put(shop, new Stock(shop));
            markDirty();
        }

        return stockLevels.get(shop);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("Shops", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            Department shop = Department.REGISTRY.get(tag.getString("Shop"));
            if (shop != null) {
                Stock stock = new Stock(shop);
                stock.deserializeNBT(tag.getCompoundTag("Stock"));
                stockLevels.put(shop, stock);
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (Map.Entry<Department, Stock> entry: stockLevels.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("Shop", Objects.requireNonNull(entry.getKey().id()));
            tag.setTag("Stock", entry.getValue().serializeNBT());
            list.appendTag(tag);
        }

        nbt.setTag("Shops", list);
        return nbt;
    }
}
