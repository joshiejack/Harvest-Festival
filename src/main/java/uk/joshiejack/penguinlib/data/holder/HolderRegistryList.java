package uk.joshiejack.penguinlib.data.holder;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;

public class HolderRegistryList {
    private final List<Holder> list = Lists.newArrayList();

    public void add(Holder holder) {
        if (!holder.isEmpty()) {
            list.add(holder);
        }
    }

    public void addAll(Collection<Holder> holders) {
        list.addAll(holders);
    }

    public boolean contains(Holder holder) {
        if (list.contains(holder)) return true;
        for (ItemStack stack: holder.getStacks()) {
            if (contains(stack)) return true;
        }

        return false;
    }

    public boolean contains(ItemStack stack) {
        for (Holder holder: list) {
            if (holder.matches(stack)) return true;
        }

        return false;
    }

    public void clear() {
        list.clear();
    }

    public List<Holder> getList() {
        return list;
    }
}
