package uk.joshiejack.penguinlib.client.gui.book.button;

import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.util.Strings;

public abstract class ButtonTextField extends ButtonBook {
    private final int maxLength;
    private int ticker;

    public ButtonTextField(GuiBook gui, int buttonId, int x, int y, int maxLength) {
        super(gui, buttonId, x, y, Strings.EMPTY);
        this.maxLength = maxLength;
        this.width = 102;
        this.height = 24;
    }

    public abstract String getText();
    public abstract void setText(String text);

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        gui.getPage().setInFocus(this); //Mark this field as in focus
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            GlStateManager.pushMatrix();
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(gui.left);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(x, y, 0, 169, 102, 24);

            String text = getText();
            if (gui.getPage().getFocus() != null) {
                if (fontrenderer.getBidiFlag()) {
                    text = text + "_";
                } else if (ticker / 6 % 2 == 0) {
                    text = text + "" + TextFormatting.BLACK + "_";
                } else {
                    text = text + "" + TextFormatting.GRAY + "_";
                }
            }

            mc.fontRenderer.drawString(text, x + 8, y + 8, 0x3E2B26);
            GlStateManager.color(1F, 1F, 1F, 1F);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public void updateScreen() {
        ticker++;
    }

    public void keyTyped(char typedChar, int keyCode) {
        String text = getText();
        switch (keyCode) {
            case 14:
                if (!text.isEmpty()) {
                    setText(text.substring(0, text.length() - 1));
                    gui.setPage(gui.getPage());
                }

                return;
            case 28:
            case 156:
                if (!text.isEmpty()) {
                    setText(text);
                    gui.setPage(gui.getPage());
                }

                return;
            default:
                if (text.length() < maxLength && ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    setText(text + typedChar);
                    gui.setPage(gui.getPage());
                }
        }
    }
}
