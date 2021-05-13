package uk.joshiejack.penguinlib.client.gui;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.renderer.font.FancyFontRenderer;
import uk.joshiejack.penguinlib.inventory.ContainerPenguinInventory;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiGreenScreen extends GuiPenguinContainer {
    private static final ResourceLocation TEXTURE = GuiElements.getTexture(PenguinLib.MOD_ID, "greenscreen");
    public GuiGreenScreen(ContainerPenguinInventory container) {
        super(container, TEXTURE, 56);
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        drawRect(0, 0, 1000, 1000, 0xFF00FF00);
        GlStateManager.color(1F, 1F, 1F);
        FancyFontRenderer.render(this, guiLeft, guiTop, "A New Beginning", 1F);
        super.drawScreen(x, y, partialTicks);
    }
}