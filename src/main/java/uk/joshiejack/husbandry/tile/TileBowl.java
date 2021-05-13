package uk.joshiejack.husbandry.tile;

import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.husbandry.item.HusbandryItems;
import uk.joshiejack.husbandry.item.ItemFeed;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@PenguinLoader("food_bowl")
public class TileBowl extends TileFoodSupply {
    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        if (stack.getItem() != HusbandryItems.FEED) return false;
        else {
            ItemFeed.Feed feed = HusbandryItems.FEED.getEnumFromStack(stack);
            return feed == ItemFeed.Feed.CAT_FOOD || feed == ItemFeed.Feed.DOG_FOOD || feed == ItemFeed.Feed.RABBIT_FOOD;
        }
    }

    @Override
    public BlockTray.Tray getTrayState() {
        ItemStack stack = handler.getStackInSlot(0);
        if (stack.isEmpty()) return BlockTray.Tray.BOWL_EMPTY;
        else {
            ItemFeed.Feed feed = HusbandryItems.FEED.getEnumFromStack(stack);
            return feed == ItemFeed.Feed.CAT_FOOD ? BlockTray.Tray.BOWL_CAT : feed == ItemFeed.Feed.DOG_FOOD ? BlockTray.Tray.BOWL_DOG : BlockTray.Tray.BOWL_RABBIT;
        }
    }
}
