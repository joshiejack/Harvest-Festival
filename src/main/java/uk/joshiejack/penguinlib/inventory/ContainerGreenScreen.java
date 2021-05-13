package uk.joshiejack.penguinlib.inventory;

import net.minecraft.entity.player.InventoryPlayer;

public class ContainerGreenScreen extends ContainerPenguinInventory {
    public ContainerGreenScreen(InventoryPlayer inventory) {
        super(0);
        bindPlayerInventory(inventory, 0);
    }
}
