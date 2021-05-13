package uk.joshiejack.economy.client;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import uk.joshiejack.economy.shipping.Shipping;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@SideOnly(Side.CLIENT)
public class Shipped {
    private final Set<Shipping.HolderSold> sold = Sets.newHashSet();
    private final List<Shipping.HolderSold> lastSold = Lists.newLinkedList();
    private static final Shipped INSTANCE = new Shipped();
    private final Cache<ItemStack, Integer> countCache = CacheBuilder.newBuilder().build();

    public static List<Shipping.HolderSold> getShippingLog() {
        return INSTANCE.lastSold;
    }

    public static Set<Shipping.HolderSold> getSold() {
        return INSTANCE.sold;
    }

    public static void clearCountCache() {
        INSTANCE.countCache.invalidateAll();
    }

    public static int getCount(ItemStack stack) {
        try {
            return INSTANCE.countCache.get(stack, () -> {
                int total = 0;
                for (Shipping.HolderSold s: INSTANCE.sold) {
                    if (s.matches(stack)) return s.getStack().getCount();
                }

                return total;
            });
        } catch (ExecutionException e) {
            return 0;
        }
    }
}
