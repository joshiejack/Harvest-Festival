package joshie.harvest.player.tracking;

import com.google.common.collect.Multimap;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.ISizeable;
import joshie.harvest.api.core.ISizedProvider;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropProvider;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.helpers.NBTHelper.ISaveable;
import joshie.harvest.core.lib.Sizeable;
import joshie.harvest.core.handlers.SizeableRegistry;
import joshie.harvest.crops.Crop;
import joshie.harvest.crops.CropRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;

public class TrackingData {
    protected HashSet<CropSoldStack> cropTracker = new HashSet<>(); //Crops that have been harvested
    protected HashSet<SellHolderStack> sellTracker = new HashSet<>(); //Items That have been sold
    protected HashSet<ItemStackHolder> obtained = new HashSet<>(); //Items that have been obtained


    //TODO: Track Mystril tools and blessed tools
    public void addAsObtained(ItemStackHolder stack) {
        obtained.add(stack);
    }

    public boolean hasObtainedItem(ItemStack stack) {
        return obtained.contains(new ItemStackHolder(stack));
    }

    public abstract static class HolderMapStack<C, K, V> extends AbstractHolder<C, K> {
        public abstract V getValue();
    }

    public abstract static class AbstractHolder<C, K> implements ISaveable {
        public AbstractHolder() {}

        public abstract K getKey();

        public void merge(C stack) {}

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof AbstractHolder) || getKey() == null || ((AbstractHolder)obj).getKey() == null) return false;
            return (((AbstractHolder) obj).getKey()).equals(getKey());
        }

        @Override
        public int hashCode() {
            return getKey() == null ? 0: getKey().hashCode();
        }
    }

    public static class BlockStack extends AbstractHolder<BlockStack, BlockPos> {
        public BlockPos pos;

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            pos = NBTHelper.readBlockPos("Pos", tag);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            NBTHelper.writeBlockPos("Pos", tag, pos);
            return tag;
        }

        @Override
        public BlockPos getKey() {
            return pos;
        }
    }

    public static class CropSoldStack extends HolderMapStack<CropSoldStack, Crop, Integer> {
        public Crop crop;
        public int amount; //Amount of this item sold

        public CropSoldStack() {}
        public CropSoldStack(ICrop crop) {
            this.crop = (Crop) crop;
            this.amount = 1;
        }

        @Override
        public Crop getKey() {
            return crop;
        }

        @Override
        public Integer getValue() {
            return amount;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            crop = CropRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("CropResource")));
            amount = tag.getInteger("SellAmount");
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            if (crop != null) {
                tag.setString("CropResource", crop.getRegistryName().toString());
                tag.setInteger("SellAmount", amount);
            }

            return tag;
        }
    }

    public static class SellHolderStack extends ItemStackHolder<SellHolderStack> {
        public int amount; //Amount of this item sold
        public long sell; //Amount of money made from this item

        public SellHolderStack() {}
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
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            super.writeToNBT(tag);
            tag.setInteger("SellAmount", amount);
            tag.setLong("SellValue", sell);
            return tag;
        }
    }

    public abstract static class AbstractItemHolder<G, T> extends AbstractHolder<G, T> {
        public static AbstractItemHolder getStack(ItemStack stack) {
            if (stack.getItem() instanceof ICropProvider) return new CropHolder(((ICropProvider)stack.getItem()).getCrop(stack));
            else if (stack.getItem() instanceof ISizedProvider) return new SizeableHolder(((ISizedProvider)stack.getItem()).getSizeable(stack));
            else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return new ItemHolder(stack.getItem());
            else return new ItemStackHolder(stack);
        }

        public static AbstractItemHolder getHolder(ItemStack stack, Multimap<Item, AbstractItemHolder> keyMap) {
            for (AbstractItemHolder holder: keyMap.get(stack.getItem())) {
                if (holder.matches(stack)) return holder;
            }

            return null;
        }

        public abstract boolean matches(ItemStack stack);
    }

    public static class CropHolder extends AbstractItemHolder<CropHolder, Crop> {
        private Crop crop;

        public CropHolder() {}
        public CropHolder (ICrop crop) {
            this.crop = (Crop) crop;
        }

        @Override
        public Crop getKey() {
            return crop;
        }

        @Override
        public boolean matches(ItemStack stack) {
            ICrop container = HFApi.crops.getCropFromStack(stack);
            if (container != null) {
                return container == crop;
            }

            return false;
        }


        @Override
        public void readFromNBT(NBTTagCompound tag) {
            crop = CropRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("CropResource")));
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            if (crop != null) {
                tag.setString("CropResource", crop.getRegistryName().toString());
            }

            return tag;
        }
    }

    public static class SizeableHolder extends AbstractItemHolder<SizeableHolder, ISizeable> {
        private Sizeable sizeable;

        public SizeableHolder() {}
        public SizeableHolder (ISizeable sizeable) {
            this.sizeable = (Sizeable) sizeable;
        }

        @Override
        public ISizeable getKey() {
            return sizeable;
        }

        @Override
        public boolean matches(ItemStack stack) {
            return HFApi.sizeable.getSizeableFromStack(stack).getLeft() == sizeable;
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            sizeable = SizeableRegistry.REGISTRY.getObject(new ResourceLocation(tag.getString("Sizeable")));
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            tag.setString("Sizeable", SizeableRegistry.REGISTRY.getNameForObject(sizeable).toString());
            return tag;
        }
    }

    public static class ItemHolder extends AbstractItemHolder<ItemHolder, Item> {
        private Item item;

        public ItemHolder() {}
        public ItemHolder(Item item) {
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
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            tag.setString("ItemName", Item.REGISTRY.getNameForObject(item).toString());
            return tag;
        }
    }

    public static class ItemStackHolder<T> extends AbstractItemHolder<T, Pair<Item, Integer>> {
        private Item item;
        private int meta;

        public ItemStackHolder() { }
        public ItemStackHolder(Item item, int meta) {
            this.item = item;
            this.meta = meta;
        }

        public ItemStackHolder(ItemStack stack) {
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
        public NBTTagCompound writeToNBT(NBTTagCompound tag) {
            tag.setString("ItemName", Item.REGISTRY.getNameForObject(item).toString());
            tag.setInteger("ItemMeta", meta);
            return tag;
        }
    }
}