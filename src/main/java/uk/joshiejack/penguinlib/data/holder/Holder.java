package uk.joshiejack.penguinlib.data.holder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class Holder implements INBTSerializable<NBTTagCompound> {
    public static final Map<String, Holder> TYPES = Maps.newHashMap();
    private static final Cache<String, Holder> cache = CacheBuilder.newBuilder().build();
    private final NonNullList<ItemStack> list = NonNullList.create();
    protected String name;

    public Holder(String name) {
        this.name = name;
    }

    public Holder register() {
        TYPES.put(name, this);
        return this;
    }

    public String name() {
        return name;
    }

    public static List<Holder> getAsAll(ItemStack stack) {
        List<Holder> list = Lists.newArrayList();
        TYPES.values().forEach(t -> {
            Holder holder = t.create(stack);
            if (holder != null) {
                list.add(holder);
            } else list.addAll(t.createList(stack));
        });

        return list;
    }

    public Holder create(ItemStack stack) {
        return null;
    }

    public List<Holder> createList(ItemStack stack) {
        return Lists.newArrayList(create(stack));
    }

    public NonNullList<ItemStack> getStacks() {
        if (list.isEmpty()) {
            for (ItemStack stack : StackHelper.getAllItemsCopy()) {
                if (matches(stack) && !list.contains(stack)) list.add(stack);
            }
        }

        return list;
    }

    public abstract boolean matches(ItemStack stack);

    public static Holder getFromStack(ItemStack stack) {
        if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) return new HolderItem(stack.getItem());
        else return new HolderMeta(stack);
    }

    private static Holder fromString(String item) {
        if (!item.contains(":")) {
            String oreName = item.startsWith("ore#") ? item.substring(4) : item;
            HolderOre.MatchType type = oreName.startsWith("*") ? HolderOre.MatchType.PREFIX : oreName.endsWith("*") ? HolderOre.MatchType.PREFIX : HolderOre.MatchType.WHOLE;
            ItemStack stack = item.startsWith("ore#") ? ItemStack.EMPTY : StackHelper.getStackFromString(item);
            return stack.isEmpty() ?  new HolderOre(oreName.replace("*", "")).setMatchType(type): item.contains(" ") || StackHelper.isSynonymn(item) ? new HolderMeta(stack) : new HolderItem(stack.getItem());
        } else {
            ItemStack stack = StackHelper.getStackFromString(item);
            if (stack.isEmpty()) return HolderItem.EMPTY;
            if (item.contains(" ")) return new HolderMeta(stack);
            else return new HolderItem(stack.getItem());
        }
    }

    @Nonnull
    public static Holder getFromString(String item) {
        try {
            return cache.get(item, () -> fromString(item));
        } catch (ExecutionException e) {
            return fromString(item);
        }
    }

    public boolean isEmpty() {
        return false;
    }
}
