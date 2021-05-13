package uk.joshiejack.husbandry.tile;

import uk.joshiejack.husbandry.client.renderer.tile.TroughRenderData;
import uk.joshiejack.husbandry.item.HusbandryItems;
import uk.joshiejack.husbandry.item.ItemFeed;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetInventorySlot;
import uk.joshiejack.penguinlib.tile.inventory.TileConnectable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.handlers.ValidatedStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemHandlerHelper;

@PenguinLoader("trough")
public class TileTrough extends TileConnectable<TileTrough> {
    private final TroughRenderData renderData = new TroughRenderData();
    public TileTrough() {
        super(2);
    }

    public TroughRenderData getRenderData() {
        return renderData;
    }

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        if (stack.getItem() != HusbandryItems.FEED) return false;
        else {
            ItemFeed.Feed feed = HusbandryItems.FEED.getEnumFromStack(stack);
            return feed == ItemFeed.Feed.FODDER || feed == ItemFeed.Feed.SLOP;
        }
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return false;
    }

    public ItemFeed.Feed getType() {
        ItemStack inventory = getMasterBlock().getStack();
        if (inventory.isEmpty()) return null;
        else {
            return HusbandryItems.FEED.getEnumFromStack(inventory);
        }
    }

    public void consume() {
        TileConnectable<?> master = getMasterBlock();
        master.getStack().shrink(1);
        PenguinNetwork.sendToNearby(master, new PacketSetInventorySlot(master.getPos(), 0, master.getStack()));
    }

    @Override
    protected ValidatedStackHandler createHandler(int size) {
        return new ValidatedStackHandler(this, size) {
            @Override
            public int getSlotLimit(int slot) {
                return (1 + members) * 16;
            }
        };
    }

    @Override
    protected boolean isSameTile(BlockPos pos) {
        return world.getTileEntity(pos) instanceof TileTrough;
    }

    @Override
    public void onSurroundingsChanged() {
        super.onSurroundingsChanged();
        renderData.reset();
    }

    @Override
    protected boolean onMasterClicked(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (isStackValidForInsertion(0, held)) {
            ItemStack result = ItemHandlerHelper.insertItem(handler, held, false);
            if (result.isEmpty() || result.getCount() != held.getCount()) {
                player.setHeldItem(hand, result);
                markDirty();
                return true;
            }
        }

        return false;
    }
}
