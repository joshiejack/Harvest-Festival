package joshie.harvest.core.gui;

import joshie.harvest.core.base.gui.GuiBaseContainer;
import joshie.harvest.core.entity.EntityBasket;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class GuiBasket extends GuiBaseContainer {
    public GuiBasket(InventoryPlayer playerInv, @Nonnull ItemStack basket, EntityBasket entityBasket) {
        super(new ContainerBasket(playerInv, basket, entityBasket), "basket", 0);
    }
}
