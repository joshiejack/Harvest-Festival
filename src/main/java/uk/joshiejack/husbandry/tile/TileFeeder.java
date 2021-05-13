package uk.joshiejack.husbandry.tile;

import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.husbandry.item.HusbandryItems;
import uk.joshiejack.husbandry.item.ItemFeed;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
@PenguinLoader("feeder")
public class TileFeeder extends TileFoodSupply {
    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return stack.getItem() == HusbandryItems.FEED && HusbandryItems.FEED.getEnumFromStack(stack) == ItemFeed.Feed.BIRD_FEED;
    }

    @Override
    public BlockTray.Tray getTrayState() {
        ItemStack stack = handler.getStackInSlot(0);
        return stack.isEmpty() ? BlockTray.Tray.FEEDER_EMPTY : stack.getCount() >= 12 ? BlockTray.Tray.FEEDER_FULL: BlockTray.Tray.FEEDER_SEMI;
    }
}
