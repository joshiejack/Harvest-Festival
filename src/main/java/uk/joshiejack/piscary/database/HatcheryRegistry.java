package uk.joshiejack.piscary.database;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import uk.joshiejack.penguinlib.data.holder.HolderMeta;

public class HatcheryRegistry {
    private static final Object2IntMap<HolderMeta> registry = new Object2IntOpenHashMap<>();

    public static void register(ItemStack stack, int value) {
        registry.put(new HolderMeta(stack), value);
    }

    public static int getValue(ItemStack stack) {
        HolderMeta entry = new HolderMeta(stack);
        return registry.containsKey(entry) ? registry.getInt(entry) : 0;
    }
}
