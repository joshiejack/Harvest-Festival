package uk.joshiejack.economy.shop.condition;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import uk.joshiejack.economy.api.shops.Condition;
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
public class ConditionShipped extends Condition {
    private List<ItemStack> stacks = Lists.newArrayList();
    private int count;

    @Override
    public Condition create(Row data, String id) {
        ConditionShipped condition = new ConditionShipped();
        condition.count = Integer.parseInt(CharMatcher.inRange('0', '9').retainFrom(id)); //Grab the required count from the id
        condition.stacks.add(data.item());
        return condition;
    }

    @Override
    public void merge(Row data) {
        stacks.add(data.item());
    }

    private Set<Shipping.HolderSold> getHolderSet(World world, EntityPlayer player) {
        return world.isRemote ? Shipped.getSold() : Market.get(world).getShippingForPlayer(player).getSold();
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        int total = 0;
        Set<Shipping.HolderSold> sold = getHolderSet(target.world, target.player);
        for (ItemStack stack : stacks) {
            for (Shipping.HolderSold holder : sold) {
                if (holder.matches(stack)) {
                    total += holder.getStack().getCount();
                    //Return early if this is true, no need to keep counting............
                    if (total >= count) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
