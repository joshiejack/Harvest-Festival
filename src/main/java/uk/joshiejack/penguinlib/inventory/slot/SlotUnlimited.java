package uk.joshiejack.penguinlib.inventory.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SlotUnlimited extends SlotPenguin {
    public SlotUnlimited(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return this.getSlotStackLimit();
    }
}
