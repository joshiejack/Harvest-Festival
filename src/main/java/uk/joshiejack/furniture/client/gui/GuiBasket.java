package uk.joshiejack.furniture.client.gui;

import uk.joshiejack.furniture.Furniture;
import uk.joshiejack.penguinlib.inventory.ContainerPenguinInventory;
import uk.joshiejack.penguinlib.client.gui.GuiPenguinContainer;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.util.ResourceLocation;


public class GuiBasket extends GuiPenguinContainer {
    private static final ResourceLocation TEXTURE = GuiElements.getTexture(Furniture.MODID, "basket");
    public GuiBasket(ContainerPenguinInventory container) {
        super(container, TEXTURE, 0);
    }
}
