package uk.joshiejack.penguinlib.client.gui;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiChatter extends GuiPenguin {
    private static final ResourceLocation TEXTURE = GuiElements.getTexture(PenguinLib.MOD_ID, "chatterbox");
    private final Chatter script;

    public GuiChatter(String text) {
        super(null);
        this.xSize = 256;
        this.ySize = 256;
        this.script = new Chatter(text);
    }

    @Override
    public void drawDefaultBackground() {}

    @Override
    public void drawBackground(int x, int y) {
        GlStateManager.color(1F, 1F, 1F);
        mc.renderEngine.bindTexture(TEXTURE);
        drawTexturedModalRect(x, sr.getScaledHeight() - 101, 0, 140, 256, 71);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        script.update(fontRenderer);
    }

    @Override
    public void drawForeground(int x, int y) {
        script.draw(fontRenderer, 20, (sr.getScaledHeight() / 2) + 42, 0x382A22);
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        super.mouseClicked(x, y, mouseButton);
        if (selectedButton == null && script.mouseClicked(mouseButton)) {
            mc.player.closeScreen();
        }
    }
}
