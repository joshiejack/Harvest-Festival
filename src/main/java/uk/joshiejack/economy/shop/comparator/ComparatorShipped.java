package uk.joshiejack.economy.shop.comparator;

import com.google.common.collect.Lists;
import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.ShopTarget;
import uk.joshiejack.economy.client.Shipped;
import uk.joshiejack.economy.shipping.Market;
import uk.joshiejack.economy.shipping.Shipping;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

@PenguinLoader("shipped")
public class ComparatorShipped extends Comparator {
    private List<ItemStack> stacks = Lists.newArrayList();

    @Override
    public Comparator create(Row data, String id) {
        ComparatorShipped comparator = new ComparatorShipped();
        comparator.stacks.add(data.item());
        return comparator;
    }

    @Override
    public void merge(Row data) {
        stacks.add(data.item());
    }

    private Set<Shipping.HolderSold> getHolderSet(World world, EntityPlayer player) {
        return world.isRemote ? Shipped.getSold() : Market.get(world).getShippingForPlayer(player).getSold();
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        int total = 0;
        Set<Shipping.HolderSold> sold = getHolderSet(target.world, target.player);
        for (ItemStack stack : stacks) {
            for (Shipping.HolderSold holder: sold) {
                if (holder.matches(stack)) {
                    total += holder.getStack().getCount();
                }
            }
        }

        return total;
    }
}
