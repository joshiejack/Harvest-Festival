package uk.joshiejack.husbandry.tile;

import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetInventorySlot;
import uk.joshiejack.penguinlib.tile.inventory.TileSingleStack;
import uk.joshiejack.penguinlib.util.handlers.ValidatedStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class TileFoodSupply extends TileSingleStack {
    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return false;
    }

    public void consume() {
        handler.getStackInSlot(0).shrink(1);
        PenguinNetwork.sendToNearby(this, new PacketSetInventorySlot(pos, 0, handler.getStackInSlot(0)));
    }

    public abstract BlockTray.Tray getTrayState();

    @Override
    protected ValidatedStackHandler createHandler(int size) {
        return new ValidatedStackHandler(this, size) {
            @Override
            public int getSlotLimit(int slot)
            {
                return 16;
            }
        };
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        if (isStackValidForInsertion(0, held)) {
            ItemStack result = ItemHandlerHelper.insertItem(handler, held, false);
            if (result.isEmpty() || result.getCount() != held.getCount()) {
                player.setHeldItem(hand, result);
                return true;
            }
        }

        return false;
    }
}
