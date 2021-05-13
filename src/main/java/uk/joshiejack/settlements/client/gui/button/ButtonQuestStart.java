package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.network.block.PacketStartScript;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ButtonQuestStart extends GuiButton {
    private final ResourceLocation quest;
    private final int startX;

    public ButtonQuestStart(ResourceLocation quest, int x, int y) {
        super(0, x, y, "");
        this.quest = quest;
        this.width = 70;
        this.height = 30;
        this.startX = 0;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            //mc.getTextureManager().bindTexture(GuiLetter.TEXTURE); //TODO: RE-Enabled/???
            drawTexturedModalRect(x, y, startX + (hovered ? width : 0), 226, width, height);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PenguinNetwork.sendToServer(new PacketStartScript(quest));
        Minecraft.getMinecraft().player.closeScreen();
    }
}
