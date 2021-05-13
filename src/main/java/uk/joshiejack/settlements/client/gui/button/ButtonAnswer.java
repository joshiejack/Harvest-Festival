package uk.joshiejack.settlements.client.gui.button;

import uk.joshiejack.settlements.client.gui.GuiNPCAsk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import javax.annotation.Nonnull;

public class ButtonAnswer extends GuiButton {
    private final GuiNPCAsk gui;
    private final int selectionID;

    public ButtonAnswer(GuiNPCAsk gui, int selectionID, int buttonId, int x, int y) {
        super(buttonId, x, y, "");
        this.width = 217;
        this.height = 10;
        this.gui = gui;
        this.selectionID = selectionID;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {}

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (gui.selected == selectionID) {
            gui.setFinished(); //Mark it as finished
        } else gui.selected = selectionID;
    }
}
