package uk.joshiejack.settlements.client.gui.label;

import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class LabelTownLaws extends LabelBook {
    public LabelTownLaws(GuiBook gui, int x, int y, Town<?> town) {
        super(gui, x, y);
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int r, int s) {
        this.drawCenteredString(mc.fontRenderer, TextFormatting.UNDERLINE + "Town Laws", x + 61, y - 7, 0xFFFFFF);
    }
}
