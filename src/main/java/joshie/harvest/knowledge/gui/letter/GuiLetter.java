package joshie.harvest.knowledge.gui.letter;

import joshie.harvest.api.core.Letter;
import joshie.harvest.core.base.gui.ContainerNull;
import joshie.harvest.core.base.gui.GuiBaseContainer;
import joshie.harvest.knowledge.letter.LetterHelper;
import net.minecraft.entity.player.EntityPlayer;

public class GuiLetter extends GuiBaseContainer {
    private final Letter letter;

    public GuiLetter(EntityPlayer player) {
        super(new ContainerNull(), "letter", 34);
        letter = LetterHelper.getMostRecentLetter(player);
    }

    @Override
    public void initGui() {
        super.initGui();
        letter.initGui(buttonList, guiLeft, guiTop);
    }

    @Override
    public void drawForeground(int x, int y) {
        letter.renderLetter(this, this.fontRendererObj, x, y);
    }
}
