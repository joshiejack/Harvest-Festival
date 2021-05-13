package uk.joshiejack.harvestcore.client.gui.button;

import uk.joshiejack.harvestcore.client.gui.GuiLetter;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.harvestcore.network.mail.PacketLetterButtonClick;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

public class ButtonLetter extends GuiButton {
    private final Letter letter;
    private final boolean accept;
    private final int startX;

    public ButtonLetter(Letter letter, int x, int y, boolean accept) {
        super(0, x, y, "");
        this.letter = letter;
        this.accept = accept;
        this.width = accept ? 20: 14;
        this.height = 16;
        this.startX = accept ? 0: 20;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            mc.getTextureManager().bindTexture(GuiLetter.TEXTURE);
            drawTexturedModalRect(x, y, startX, 224 + (!hovered ? 16: 0), width, 16);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PenguinNetwork.sendToServer(new PacketLetterButtonClick(letter, accept));
        Minecraft.getMinecraft().player.closeScreen();
    }
}
