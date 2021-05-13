package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.network.town.land.PacketSetName;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.button.ButtonTextField;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

public class ButtonRenameTown extends ButtonTextField {
    private final Town<?> town;
    private final int dim, id;

    public ButtonRenameTown(GuiBook gui, int dim, Town<?> town, int buttonId, int x, int y) {
        super(gui, buttonId, x, y, 25);
        this.dim = dim;
        this.town = town;
        this.id = town.getID();
        this.width = 120;
        this.height = 8;
    }

    @Override
    public String getText() {
        return town.getCharter().getName();
    }

    @Override
    public void setText(String text) {
        town.getCharter().setName(text); //Update it on the client right away
        PenguinNetwork.sendToServer(new PacketSetName(dim, id, text)); //TODO: Only send the packet when the player is DONE editing, so when they change the page or whatever?
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            GlStateManager.pushMatrix();
            boolean flag = mc.fontRenderer.getUnicodeFlag();
            mc.fontRenderer.setUnicodeFlag(true);
            mc.fontRenderer.drawStringWithShadow(TextFormatting.BOLD + displayString, x, y, 0x857754);
            mc.fontRenderer.setUnicodeFlag(flag);
            GlStateManager.popMatrix();
        }
    }
}
