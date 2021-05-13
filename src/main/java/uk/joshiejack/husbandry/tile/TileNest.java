package uk.joshiejack.husbandry.tile;

import uk.joshiejack.husbandry.block.BlockTray;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.tile.inventory.TileSingleStack;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.ItemHandlerHelper;

@SuppressWarnings("unused")
@PenguinLoader("nest")
public class TileNest extends TileSingleStack {
    public static final HolderRegistry<BlockTray.Tray> FILL_REGISTRY = new HolderRegistry<>(BlockTray.Tray.NEST_EMPTY);

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return true;
    }

    public BlockTray.Tray getTrayState() {
        return FILL_REGISTRY.getValue(handler.getStackInSlot(0));
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        if (!handler.getStackInSlot(0).isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, handler.getStackInSlot(0));
            handler.setStackInSlot(0, ItemStack.EMPTY);

            return true;
        }

        return false;
    }
}
