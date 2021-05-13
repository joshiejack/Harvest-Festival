package uk.joshiejack.furniture.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import uk.joshiejack.furniture.client.gui.GuiTelevision;
import uk.joshiejack.furniture.network.PacketSetTVChannel;
import uk.joshiejack.furniture.television.TVChannel;
import uk.joshiejack.penguinlib.client.GuiElements;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;

import javax.annotation.Nonnull;

public class ButtonChannel extends GuiButton {
    private final GuiTelevision gui;
    private final TVChannel channel;

    public ButtonChannel(GuiTelevision gui, int buttonId, int x, int y, TVChannel channel) {
        super(buttonId, x, y, "");
        this.gui = gui;
        this.channel = channel;
        this.width = 62;
        this.height = 46;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
            GlStateManager.pushMatrix();
            if (!hovered) GlStateManager.enableLighting();
            else GlStateManager.disableLighting();
            mc.getTextureManager().bindTexture(GuiElements.getTexture(channel.getScreenshot().getNamespace(), channel.getScreenshot().getPath()));
            drawTexturedModalRect(x, y, channel.getScreenshotX(), channel.getScreenshotY(), width, height);
            GlStateManager.popMatrix();
            if (hovered) gui.addTooltip(StringHelper.localize(channel.getUnlocalizedName()));
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PenguinNetwork.sendToServer(new PacketSetTVChannel(gui.television.getPos(), channel));
        Minecraft.getMinecraft().player.closeScreen();
    }
}
