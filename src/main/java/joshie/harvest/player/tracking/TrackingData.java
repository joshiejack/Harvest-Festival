package joshie.harvest.player.tracking;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.core.helpers.NBTHelper.ISaveable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;

public class TrackingData {
    HashSet<CropHolderStack> cropTracker = new HashSet<>(); //Crops that have been harvested
    HashSet<SellHolderStack> sellTracker = new HashSet<>(); //Items That have been sold
    HashSet<ItemHolderStack> obtained = new HashSet<>(); //Items that have been obtained


    //TODO: Track Mystril tools and blessed tools
    public void addAsObtained(ItemHolderStack stack) {
        obtained.add(stack);
    }

    public boolean hasObtainedItem(ItemStack stack) {
        return obtained.contains(new ItemHolderStack(stack));
    }

    public abstract static class HolderMapStack<C, K, V> extends HolderStack<C, K> {
        public abstract V getValue();
    }

    public abstract static class HolderStack<C, K> implements ISaveable {
        public HolderStack() {}

        public abstract K getKey();

        public void merge(C stack) {}

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof HolderStack)) return false;
            return (((HolderStack) obj).getKey()).equals(getKey());
        }

        @Override
        public int hashCode() {
            return getKey().hashCode();
        }
    }

    public static class IntegerHolderStack extends HolderStack<IntegerHolderStack, Integer> {
        public int value;

        public IntegerHolderStack(int i) {
            this.value = i;
        }

        @Override
        public void merge(IntegerHolderStack stack) {
            value += stack.value;
        }

        @Override
        public Integer getKey() {
            return value;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            value = tag.getInteger("Amount");
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setInteger("Amount", value);
        }
    }

    public static class CropHolderStack extends HolderMapStack<CropHolderStack, ICrop, Integer> {
        public ICrop crop;
        public int amount; //Amount of this item sold

        public CropHolderStack (ICrop crop) {
            amount = 1;
        }

        @Override
        public ICrop getKey() {
            return crop;
        }

        @Override
        public Integer getValue() {
            return amount;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            crop = HFApi.CROPS.getCrop(new ResourceLocation(tag.getString("CropResource")));
            amount = tag.getInteger("SellAmount");
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setString("CropResource", crop.getResource().toString());
            tag.setInteger("SellAmount", amount);
        }
    }

    public static class SellHolderStack extends ItemHolderStack<SellHolderStack> {
        public int amount; //Amount of this item sold
        public long sell; //Amount of money made from this item

        public SellHolderStack(ItemStack stack) {
            super(stack);
        }

        public SellHolderStack(ItemStack stack, long sell) {
            super(stack);
            amount = stack.stackSize;
            this.sell = sell;
        }

        @Override
        public void merge(SellHolderStack stack) {
            amount += stack.amount;
            sell += stack.sell;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            super.readFromNBT(tag);
            amount = tag.getInteger("SellAmount");
            sell = tag.getLong("SellValue");
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            tag.setInteger("SellAmount", amount);
            tag.setLong("SellValue", sell);
        }
    }

    public abstract static class GiftHolder<G, T> extends HolderStack<G, T> {
        private ItemStack stack;
        public GiftHolder(ItemStack stack) {
            this.stack = stack;
        }

        public abstract boolean matches(ItemStack stack);
    }

    public static class ItemHolder extends GiftHolder<ItemHolder, Item> {
        private Item item;

        public ItemHolder(Item item) {
            super(null);
            this.item = item;
        }

        @Override
        public Item getKey() {
            return item;
        }

        @Override
        public boolean matches(ItemStack stack) {
            return stack.getItem() == item;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            item = Item.REGISTRY.getObject(new ResourceLocation(tag.getString("ItemName")));
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setString("ItemName", Item.REGISTRY.getNameForObject(item).toString());
        }
    }

    public static class ItemHolderStack<T> extends GiftHolder<T, Pair<Item, Integer>> {
        private Item item;
        private int meta;

        public ItemHolderStack(Item item, int meta) {
            super(null);
            this.item = item;
            this.meta = meta;
        }

        public ItemHolderStack(ItemStack stack) {
            super(null);
            this.item = stack.getItem();
            this.meta = stack.getItemDamage();
        }

        @Override
        public Pair<Item, Integer> getKey() {
            return Pair.of(item, meta);
        }

        @Override
        public boolean matches(ItemStack stack) {
            return stack.getItem() == item && stack.getItemDamage() == meta;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            item = Item.REGISTRY.getObject(new ResourceLocation(tag.getString("ItemName")));
            meta = tag.getInteger("ItemMeta");
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setString("ItemName", Item.REGISTRY.getNameForObject(item).toString());
            tag.setInteger("ItemMeta", meta);
        }
    }
}