package harvestmoon.gui;

import harvestmoon.blocks.TileFridge;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerCooking extends ContainerBase {
    //The Fridge CAN be null
    private TileFridge fridge;
    
    public ContainerCooking(TileFridge fridge, InventoryPlayer playerInventory) {
        this.fridge = fridge;
    }
}
