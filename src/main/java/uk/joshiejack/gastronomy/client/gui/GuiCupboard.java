package uk.joshiejack.gastronomy.client.gui;

import uk.joshiejack.gastronomy.Gastronomy;
import uk.joshiejack.penguinlib.inventory.ContainerPenguinInventory;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.util.ResourceLocation;

public class GuiCupboard extends GuiFoodStorage {
    private static final ResourceLocation TEXTURE = GuiElements.getTexture(Gastronomy.MODID, "cupboard");

    public GuiCupboard(ContainerPenguinInventory container) {
        super(container, TEXTURE, 2);
    }
}
