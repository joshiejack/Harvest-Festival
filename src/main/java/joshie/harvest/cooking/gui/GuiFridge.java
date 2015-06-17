package joshie.harvest.cooking.gui;

import joshie.harvest.core.util.GuiBase;
import joshie.harvest.player.fridge.FridgeData;
import net.minecraft.inventory.IInventory;

public class GuiFridge extends GuiBase {
    private final FridgeData fridge;

    public GuiFridge(IInventory playerInv, FridgeData fridge) {
        super(new ContainerFridge(playerInv, fridge), "fridge", 0);
        this.fridge = fridge;
    }
}
