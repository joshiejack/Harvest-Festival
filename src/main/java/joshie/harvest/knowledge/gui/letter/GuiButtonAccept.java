package joshie.harvest.knowledge.gui.letter;

import joshie.harvest.api.core.Letter;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.knowledge.packet.PacketButtonAccept;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

public class GuiButtonAccept extends GuiButton {
    private final Letter letter;

    public GuiButtonAccept(Letter letter, int x, int y) {
        super(0, x, y, "");
        this.letter = letter;
        this.width = 20;
        this.height = 16;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            mc.getTextureManager().bindTexture(GuiLetter.LETTER_TEXTURE);
            drawTexturedModalRect(x, y, 0, 224 + (!hovered ? 16: 0), 20, 16);
            mouseDragged(mc, mouseX, mouseY);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PacketHandler.sendToServer(new PacketButtonAccept(letter));
    }
}
