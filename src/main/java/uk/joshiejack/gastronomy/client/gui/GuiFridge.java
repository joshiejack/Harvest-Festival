package uk.joshiejack.gastronomy.client.gui;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.gastronomy.inventory.ContainerFridge;
import uk.joshiejack.penguinlib.client.GuiElements;

import static uk.joshiejack.gastronomy.Gastronomy.MODID;

public class GuiFridge extends GuiFoodStorage {
    private static final ResourceLocation TEXTURE = GuiElements.getTexture(MODID, "fridge");

    public GuiFridge(ContainerFridge container) {
        super(container, TEXTURE, 82);
    }
}
