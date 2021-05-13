package uk.joshiejack.settlements.client.gui.label;

import uk.joshiejack.settlements.quest.settings.Information;
import uk.joshiejack.penguinlib.client.gui.Chatter;
import uk.joshiejack.penguinlib.client.gui.book.GuiBook;
import uk.joshiejack.penguinlib.client.gui.book.label.LabelBook;
import uk.joshiejack.penguinlib.util.helpers.minecraft.StackRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;

public class LabelQuest extends LabelBook {
    private final Information information;

    public LabelQuest(GuiBook gui, Information information, int x, int y) {
        super(gui, x, y);
        this.information = information;
    }

    @Override
    public void drawLabel(@Nonnull Minecraft mc, int r, int s) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        StackRenderHelper.drawStack(information.getIcon(), x + 2, y + 1, 1F);
        boolean flag = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);
        mc.fontRenderer.drawString(TextFormatting.BOLD + Chatter.modify(information.getName()), x + 4, y - 8, 0x857754);
        mc.fontRenderer.drawSplitString(information.getDescription(), x + 20, y - 1, 100, 0x857754);
        mc.fontRenderer.setUnicodeFlag(flag);
        drawRect(x + 4, y + 17, x + 120, y + 18, 0xFF857754);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }
}
