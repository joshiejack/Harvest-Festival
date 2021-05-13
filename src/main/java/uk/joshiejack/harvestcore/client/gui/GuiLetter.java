package uk.joshiejack.harvestcore.client.gui;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.client.gui.button.ButtonLetter;
import uk.joshiejack.harvestcore.client.mail.Inbox;
import uk.joshiejack.harvestcore.registry.letter.Letter;
import uk.joshiejack.penguinlib.client.gui.GuiPenguin;
import uk.joshiejack.penguinlib.client.GuiElements;
import net.minecraft.util.ResourceLocation;

public class GuiLetter extends GuiPenguin {
    public static final ResourceLocation TEXTURE = GuiElements.getTexture(HarvestCore.MODID, "letter");
    private final Letter letter;

    public GuiLetter() {
        super(TEXTURE);
        ySize = 200;
        letter = Inbox.getLetters().get(0);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new ButtonLetter(letter, guiLeft + 140, guiTop + 165, true));
        if (letter.isRejectable()) buttonList.add(new ButtonLetter(letter, guiLeft + 120, guiTop + 165, false));
        letter.initGui(buttonList, guiLeft, guiTop);
    }

    @Override
    public void drawForeground(int x, int y) {
        letter.renderLetter(this, this.fontRenderer, guiLeft, guiTop, x, y);
    }

    @Override
    public void onGuiClosed() {
        letter.onClosed(mc.player);
    }
}
