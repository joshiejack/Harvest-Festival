package uk.joshiejack.gastronomy.tile;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.gastronomy.block.BlockCupboard;
import uk.joshiejack.gastronomy.block.GastronomyBlocks;
import uk.joshiejack.gastronomy.inventory.slot.SlotFood;
import uk.joshiejack.penguinlib.tile.inventory.TileInventoryRotatable;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

@PenguinLoader("cupboard")
public class TileCupboard extends TileInventoryRotatable {
    public TileCupboard() {
        super(9);
    }

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return hasInventory() && SlotFood.isValid(stack);
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return !handler.getStackInSlot(slot).isEmpty() && hasInventory();
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        if (hasInventory()) {
            player.openGui(Gastronomy.instance, 0, player.world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        } else return false;
    }

    private boolean hasInventory() {
        IBlockState actual = getState().getActualState(world, pos);
        return actual.getBlock() == blockType && GastronomyBlocks.CUPBOARD.getEnumFromState(actual) == BlockCupboard.Cupboard.FULL;
    }
}
