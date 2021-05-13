package uk.joshiejack.gastronomy.inventory;

import uk.joshiejack.gastronomy.inventory.slot.SlotFood;
import uk.joshiejack.gastronomy.tile.TileFridge;
import uk.joshiejack.penguinlib.inventory.ContainerPenguinUnlimited;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerFridge extends ContainerPenguinUnlimited {
    private final TileFridge fridge;

    public ContainerFridge(InventoryPlayer inventory, TileFridge fridge) {
        super(fridge.getHandler().getSlots());
        this.fridge = fridge;
        fridge.getHandler().addPlayer();
        int offsetY = 0;
        for (int i = 0; i < 7; i++) {
            if (i == 3) offsetY++;
            for (int j = 0; j < 5; j++) {
                addSlotToContainer(new SlotFood(fridge.getHandler(), j + i * 5, 44 + j * 18, (i * 18) + 18 + (offsetY * 8)));
            }
        }

        bindPlayerInventory(inventory, 82);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        fridge.getHandler().removePlayer();
    }

    @Override
    protected boolean hasUnlimitedInventory(Slot slot) {
        return slot instanceof SlotFood;
    }

}
