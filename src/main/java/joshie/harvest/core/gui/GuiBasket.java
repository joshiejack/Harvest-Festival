package joshie.harvest.core.gui;

import joshie.harvest.core.base.gui.GuiBaseContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GuiBasket extends GuiBaseContainer {
    public GuiBasket(InventoryPlayer playerInv, ItemStack basket) {
        super(new ContainerBasket(playerInv, basket), "fridge", 56);
    }
}
