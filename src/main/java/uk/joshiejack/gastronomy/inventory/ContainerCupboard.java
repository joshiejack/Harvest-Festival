package uk.joshiejack.gastronomy.inventory;

import uk.joshiejack.gastronomy.inventory.slot.SlotFood;
import uk.joshiejack.gastronomy.tile.TileCupboard;
import uk.joshiejack.penguinlib.inventory.ContainerPenguinUnlimited;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerCupboard extends ContainerPenguinUnlimited {
    private final TileCupboard cupboard;

    public ContainerCupboard(InventoryPlayer inventory, TileCupboard cupboard) {
        super(cupboard.getHandler().getSlots());
        this.cupboard = cupboard;
        cupboard.getHandler().addPlayer();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotFood(cupboard.getHandler(), j + i * 3, 62 + j * 18, (i * 18) + 18));
            }
        }

        bindPlayerInventory(inventory, 2);
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        cupboard.getHandler().removePlayer();
    }
}
