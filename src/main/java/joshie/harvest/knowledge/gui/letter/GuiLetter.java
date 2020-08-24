package joshie.harvest.knowledge.gui.letter;

import joshie.harvest.api.core.Letter;
import joshie.harvest.core.base.gui.ContainerNull;
import joshie.harvest.core.base.gui.GuiBase;
import joshie.harvest.knowledge.letter.LetterHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import static joshie.harvest.core.lib.HFModInfo.MODID;

public class GuiLetter extends GuiBase {
    static final ResourceLocation LETTER_TEXTURE = new ResourceLocation(MODID, "textures/gui/letter.png") ;
    private final Letter letter;

    public GuiLetter(EntityPlayer player) {
        super(new ContainerNull(), "letter", 34);
        letter = LetterHelper.getMostRecentLetter(player);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButtonAccept(letter, guiLeft + 140, guiTop + 165));
        if (letter.hasRejectButton()) buttonList.add(new GuiButtonReject(letter, guiLeft + 120, guiTop + 165));
        letter.initGui(buttonList, guiLeft, guiTop);
    }

    @Override
    public void drawForeground(int x, int y) {
        letter.renderLetter(this, this.fontRenderer, x, y);
    }
}
